const red = document.querySelector("#red");
const green = document.querySelector("#green");
const blue = document.querySelector("#blue");
const redText = document.querySelector("#redText");
const greenText = document.querySelector("#greenText");
const blueText = document.querySelector("#blueText");

const body = document.querySelector("body");

function updateBackgroundColor() {
    const redValue = red.value;
    const greenValue = green.value;
    const blueValue = blue.value;

    redText.style.color = `rgb(${255 - redValue}, 0, 0)`;
    greenText.style.color = `rgb(0, ${255 - greenValue}, 0)`;
    blueText.style.color = `rgb(0, 0, ${255 - blueValue})`;

    body.style.backgroundColor = "rgb(" + redValue + ", " + greenValue + ", " + blueValue + ")";
}

red.addEventListener("input", updateBackgroundColor);
green.addEventListener("input", updateBackgroundColor);
blue.addEventListener("input", updateBackgroundColor);