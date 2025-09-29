const p = document.querySelector("p");

p.innerText = "Tuesday is the most beautiful day";
p.innerHTML = "<b>Tuesday</b> is the most beautiful day";

const h1 = document.querySelector("h1");
h1.innerText = "Hello, World";

const h1s = document.querySelectorAll("h1");
for (const h of h1s) {
    h.innerText = "Guten Tag";
}

// CSS --> background-color: cyan;
// JS ---> backgroundColor = "cyan"

p.style.backgroundColor = "cyan";
p.style.color = "red";
p.style.border = "3px solid black";

const img = document.querySelector("img");
img.src = "https://img.freepik.com/premium-photo/digital-art-photography-illustration-painting-pattern_727939-8579.jpg"
