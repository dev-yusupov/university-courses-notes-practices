const feedbackElement = document.getElementById("feedback");
const guessNumberInputField = document.getElementById("guessNumberInputField");
const submitGuessButton = document.getElementById("submitGuessButton");
const resetButton = document.querySelector("#resetButton");

let generatedNumber = generateRandomNumber();

function generateRandomNumber() {
    return Math.floor(Math.random() * 100);
}

function handleGuessedNumber(e) {
    let guessedValue = parseInt(guessNumberInputField.value);

    if (guessedValue > generatedNumber) {
        feedbackElement.innerText = "The number is less than your guess!";
    } else if (guessedValue < generatedNumber) {
        feedbackElement.innerText = "The number is greater than your guess!";
    } else {
        feedbackElement.style.color = "green";
        feedbackElement.innerHTML = "<b>You have found!</b>"
        submitGuessButton.disabled = true;
    }
}

function handleReset(e) {
    submitGuessButton.disabled = false;
    generatedNumber = generateRandomNumber();
    feedbackElement.innerText = "Guess";
    feedbackElement.style.color = "black";
}

submitGuessButton.addEventListener("click", handleGuessedNumber);
resetButton.addEventListener("click", handleReset);