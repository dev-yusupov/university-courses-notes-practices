const wordList = ["javascript", "programming"];
const visualElements = [
    `<line x1="0" y1="99%" x2="100%" y2="99%" />`,
    `<line x1="20%" y1="99%" x2="20%" y2="5%" />`,
    `<line x1="20%" y1="5%" x2="60%" y2="5%" />`,
    `<line x1="60%" y1="5%" x2="60%" y2="20%" />`,
    `<circle cx="60%" cy="30%" r="10%" />`,
    `<line x1="60%" y1="30%" x2="60%" y2="70%" />`,
    `<line x1="40%" y1="50%" x2="80%" y2="50%" />`,
    `<line x1="60%" y1="70%" x2="50%" y2="90%" />`,
    `<line x1="60%" y1="70%" x2="70%" y2="90%" />`
];

let targetWord;
let guessedLetters;
let mistakes;
let gameOver;
let startTime;

function startGame() {
    document.querySelector("#controls").style.display = "none";
    document.querySelector(".game").style.display = "block";
    targetWord = wordList[ Math.floor(Math.random() * wordList.length) ]
    guessedLetters = [];
    mistakes = 0;
    gameOver = false;
    startTime = performance.now();
    renderState();
}

function renderState() {
    checkEndGame();
    renderWord();
    renderButtons();
    renderLittleMan();
}

function checkEndGame() {
    const result = document.querySelector("#result");
    if (mistakes >= visualElements.length) {
        gameOver = true;
        result.innerText = "You lost!"
        result.style.color = "red";
    } else if (targetWord.split("").every(letter => guessedLetters.includes(letter))) {
        gameOver = true;
        const endTime = performance.now();
        const sec = Math.floor((endTime - startTime) / 1000);
        result.innerText = `You won in ${sec} seconds`;
        result.style.color = "green";
    } else {
        gameOver = false;
    }

    if (gameOver) {
        document.querySelector("#controls").style.display = "block";
    }
}

function renderWord() {
    document.querySelector("tr").innerHTML = 
        targetWord.split('').map(letter => `<td>${
            guessedLetters.includes(letter) ? letter : "_"
        }</td>`).join("");
}

function renderLittleMan() {
    document.querySelector("svg").innerHTML =
    visualElements.slice(0, mistakes).join("");
}

function renderButtons() {
    const alphabet = [...Array(26).keys()].map(x => String.fromCharCode(x + 65));

    document.querySelector("#buttons").innerHTML = 
        alphabet.map(letter => `<button id="${letter}" ${
            guessedLetters.includes(letter.toLowerCase()) ? "disabled" : ""
        }>${letter}</button>`).join("");
}

document.querySelector("#buttons").addEventListener("click", handleLetterClick);
document.querySelector("#start").addEventListener("click", startGame);

function handleLetterClick(e) {
    if (!gameOver && e.target.matches("button")) {
        const letter = e.target.innerText.toLowerCase();
        guessedLetters.push(letter);
        if (!targetWord.includes(letter)) mistakes++;
        renderState();
    } else {

    }
}

document.body.addEventListener("keypress", function(e) {
    const letterButton = document.querySelector(`button#${e.key.toUpperCase()}`);
    letterButton.dispatchEvent(new Event("click", { bubbles: true }));
})