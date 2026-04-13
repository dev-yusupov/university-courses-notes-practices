import re
from pathlib import Path


LATEX_SPECIALS = {
    "\\": r"\textbackslash{}",
    "{": r"\{",
    "}": r"\}",
    "#": r"\#",
    "$": r"\$",
    "%": r"\%",
    "&": r"\&",
    "_": r"\_",
    "~": r"\textasciitilde{}",
    "^": r"\textasciicircum{}",
}


def latex_escape(text: str) -> str:
    return "".join(LATEX_SPECIALS.get(ch, ch) for ch in text)


INLINE_CODE_RE = re.compile(r"`([^`]+)`")


def convert_inline_code(s: str) -> str:
    def repl(m: re.Match) -> str:
        # Escape inside \texttt{...}
        return "\\texttt{" + latex_escape(m.group(1)) + "}"

    # Escape everything first, then unescape inside code blocks via repl
    # Approach: split and rebuild to avoid double-escaping
    out_parts: list[str] = []
    last = 0
    for m in INLINE_CODE_RE.finditer(s):
        out_parts.append(latex_escape(s[last : m.start()]))
        out_parts.append(repl(m))
        last = m.end()
    out_parts.append(latex_escape(s[last:]))
    return "".join(out_parts)


IMG_RE = re.compile(r"!\[(?P<alt>[^\]]*)\]\((?P<path>[^)]+)\)")


def convert_stem_line(line: str) -> str:
    line = line.rstrip("\n")
    m = IMG_RE.fullmatch(line.strip())
    if m:
        img_path = m.group("path").strip()
        alt = m.group("alt").strip()
        caption = convert_inline_code(alt) if alt else ""
        # Keep it simple: centered image, optional small caption-like text
        parts = [
            "\\begin{center}",
            "\\includegraphics[width=0.85\\linewidth]{" + latex_escape(img_path) + "}",
        ]
        if caption:
            parts.append("\\\\{\\small " + caption + "}")
        parts.append("\\end{center}")
        return "\n".join(parts)
    return convert_inline_code(line)


QUESTION_START_RE = re.compile(r"^(?P<num>\d+)\.\s+(?P<text>.*)\s*$")
CHOICE_RE = re.compile(r"^(?P<correct>\*)?(?P<label>[a-zA-Z])\)\s+(?P<text>.*)\s*$")


def parse_quiz(lines: list[str]):
    title = ""
    description = ""
    questions = []

    i = 0
    while i < len(lines):
        line = lines[i].rstrip("\n")
        if line.lower().startswith("quiz title:"):
            title = line.split(":", 1)[1].strip()
            i += 1
            continue
        if line.lower().startswith("quiz description:"):
            description = line.split(":", 1)[1].strip()
            i += 1
            continue

        m_q = QUESTION_START_RE.match(line)
        if not m_q:
            i += 1
            continue

        q_num = int(m_q.group("num"))
        stem_lines = [m_q.group("text").rstrip()]
        i += 1

        # Consume indented wrapped paragraph lines for stem
        while i < len(lines):
            nxt = lines[i].rstrip("\n")
            if not nxt.strip():
                i += 1
                continue
            if QUESTION_START_RE.match(nxt):
                break
            if CHOICE_RE.match(nxt):
                break
            # treat anything else as stem continuation
            stem_lines.append(nxt.strip())
            i += 1

        # Now consume choices
        choices = []
        while i < len(lines):
            nxt = lines[i].rstrip("\n")
            m_c = CHOICE_RE.match(nxt)
            if not m_c:
                break
            choices.append(
                {
                    "label": m_c.group("label").lower(),
                    "correct": bool(m_c.group("correct")),
                    "text": m_c.group("text"),
                }
            )
            i += 1

        questions.append({"num": q_num, "stem": stem_lines, "choices": choices})

    return title, description, questions


def render_latex(title: str, description: str, questions: list[dict]) -> str:
    title_esc = latex_escape(title) if title else "Quiz"
    desc_esc = latex_escape(description)
    header = "\n".join(
        [
            "\\documentclass[11pt]{article}",
            "\\usepackage[margin=1in]{geometry}",
            "\\usepackage{graphicx}",
            "\\usepackage{enumitem}",
            "\\usepackage[T1]{fontenc}",
            "\\usepackage[utf8]{inputenc}",
            "",
            r"% Toggle this to show/hide correct answers in the output.",
            "\\newif\\ifshowanswers",
            "\\showanswersfalse",
            "",
            "\\begin{document}",
            "",
            "\\begin{center}",
            "{\\LARGE " + title_esc + "}\\\\",
            "\\vspace{0.25em}",
            "{\\normalsize " + desc_esc + "}",
            "\\end{center}",
            "",
            "\\vspace{1em}",
        ]
    )

    body_parts: list[str] = [header, "\\begin{enumerate}[leftmargin=*, label=\\textbf{\\arabic*.}]\n"]

    for q in questions:
        body_parts.append("\\item")
        # Stem
        stem_rendered = []
        for stem_line in q["stem"]:
            if stem_line.strip():
                stem_rendered.append(convert_stem_line(stem_line))
        if stem_rendered:
            body_parts.append("\n".join(stem_rendered))

        # Choices
        body_parts.append("\\begin{enumerate}[label=\\alph*)]")
        for c in q["choices"]:
            choice_text = convert_inline_code(c["text"])
            if c["correct"]:
                body_parts.append(
                    "\\item "
                    + ("\\ifshowanswers\\textbf{" + choice_text + "}\\else " + choice_text + "\\fi")
                )
            else:
                body_parts.append("\\item " + choice_text)
        body_parts.append("\\end{enumerate}\n")

    body_parts.append("\\end{enumerate}\n")

    # Optional answer key section (only if toggled)
    body_parts.append(
        "\n".join(
            [
                "",
                "\\ifshowanswers",
                "\\section*{Answer Key}",
                "\\begin{enumerate}[leftmargin=*, label=\\textbf{\\arabic*.}]",
            ]
        )
    )
    for q in questions:
        correct_labels = [c["label"] for c in q["choices"] if c["correct"]]
        ans = ", ".join(correct_labels) if correct_labels else ""
        body_parts.append("\\item " + latex_escape(ans))
    body_parts.append(
        "\n".join(
            [
                "\\end{enumerate}",
                "\\fi",
                "",
                "\\end{document}",
                "",
            ]
        )
    )

    return "\n".join(body_parts)


def main() -> int:
    in_path = Path(__file__).with_name("quiz.txt")
    out_path = Path(__file__).with_name("quiz.tex")
    out_answers_path = Path(__file__).with_name("quiz_answers.tex")

    lines = in_path.read_text(encoding="utf-8").splitlines(True)
    title, desc, questions = parse_quiz(lines)
    tex = render_latex(title, desc, questions)
    out_path.write_text(tex, encoding="utf-8")

    tex_with_answers = tex.replace("\\showanswersfalse", "\\showanswerstrue", 1)
    out_answers_path.write_text(tex_with_answers, encoding="utf-8")

    print(f"Wrote {out_path}")
    print(f"Wrote {out_answers_path}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
