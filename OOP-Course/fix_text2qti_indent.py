import re
from pathlib import Path

QUESTION_RE = re.compile(r"^\d+\.\s+\S")
CHOICE_RE = re.compile(r"^\*?[a-zA-Z]\)\s+\S")


def fix_indentation(text: str, indent: str = "    ") -> str:
    out_lines: list[str] = []
    in_question_stem = False

    for raw_line in text.splitlines(True):
        line = raw_line.rstrip("\n")
        newline = "\n" if raw_line.endswith("\n") else ""

        if QUESTION_RE.match(line):
            in_question_stem = True
            out_lines.append(line + newline)
            continue

        if in_question_stem:
            if not line.strip():
                out_lines.append(line + newline)
                continue

            # Choices end the stem
            if CHOICE_RE.match(line):
                in_question_stem = False
                out_lines.append(line + newline)
                continue

            # Any other non-empty line is a wrapped paragraph -> must be indented
            if line.startswith((" ", "\t")):
                out_lines.append(line + newline)
            else:
                out_lines.append(indent + line + newline)
            continue

        out_lines.append(line + newline)

    return "".join(out_lines)


def main() -> int:
    quiz_path = Path(__file__).with_name("quiz.txt")
    original = quiz_path.read_text(encoding="utf-8")
    fixed = fix_indentation(original)

    if fixed != original:
        quiz_path.write_text(fixed, encoding="utf-8")
        print(f"Updated indentation in {quiz_path}")
    else:
        print("No changes needed")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
