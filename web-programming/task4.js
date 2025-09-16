const a = document.querySelector("#a");
const b = document.querySelector("#b");
const btn = document.querySelector("button")
const result = document.querySelector("#result")
const ops = document.querySelector("#ops");

function calculate(a, b, op) {
    switch (op) {
        case "add":
            return parseInt(a) + parseInt(b);
        case "sub":
            return parseInt(a) - parseInt(b);
        case "mult":
            return parseInt(a) * parseInt(b);
        case "div":
            return parseInt(a) / parseInt(b);
        
        default:
            return parseInt(a) + parseInt(b);
    }
}

btn.addEventListener("click", () => {
    const aVal = a.value;
    const bVal = b.value;
    const op = ops.value;

    console.log(op);

    const res = calculate(aVal, bVal, op);

    result.innerText = res;
});