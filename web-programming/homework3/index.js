const amountSpan = document.querySelector("#amount");
const banknoteContainer = document.querySelector("#banknotes");
const table = document.querySelector("tbody");
const select = document.querySelector("select");
const transferButton = document.querySelector("button");


console.log(people);

let row1 = document.createElement("tr");
let row2 = document.createElement("tr");
let row3 = document.createElement("tr");
let row4 = document.createElement("tr");

for (let person of people) {
  const td1 = document.createElement("td");
  const img = document.createElement("img");
  img.src = `img/${person.photo}`;
  img.alt = person.name;
  td1.appendChild(img);
  row1.appendChild(td1);

  const td2 = document.createElement("td");
  td2.textContent = person.name;
  row2.appendChild(td2);

  const td3 = document.createElement("td");
  td3.textContent = person.paid.toLocaleString() + " Ft";
  // Check for overpaid on initial load
  if (person.paid > person.to_pay) {
    td3.classList.add("overpaid");
  }
  row3.appendChild(td3);

  const td4 = document.createElement("td");
  td4.textContent = person.to_pay.toLocaleString() + " Ft";
  row4.appendChild(td4);

  const option = document.createElement("option");
  option.value = person.name;
  option.textContent = person.name;
  select.appendChild(option);
}

table.appendChild(row1);
table.appendChild(row2);
table.appendChild(row3);
table.appendChild(row4);


function handleBanknoteClick(e) {
  if (e.target.tagName !== "IMG") return;
  const value = parseInt(e.target.dataset.value);
  amountSpan.textContent = (parseInt(amountSpan.textContent) + value).toString();
}

banknoteContainer.addEventListener("click", handleBanknoteClick);

table.addEventListener("click", function(e) {
  if (e.target.tagName === "IMG" && e.target.alt) {
    const personName = e.target.alt;
    select.value = personName;
  }
});

transferButton.addEventListener("click", function() {
  const selectedName = select.value;
  const amount = parseInt(amountSpan.textContent);
  if (amount === 0 || !selectedName) return;

  const person = people.find(p => p.name === selectedName);
  if (person) {
    person.paid += amount;
    amountSpan.textContent = "0";
    updateTable();
  }
});

function updateTable() {
  row3.innerHTML = "";
  row4.innerHTML = "";
  
  for (let person of people) {
    const td3 = document.createElement("td");
    td3.textContent = person.paid.toLocaleString() + " Ft";
    if (person.paid > person.to_pay) {
      td3.classList.add("overpaid");
    }
    row3.appendChild(td3);
    
    const td4 = document.createElement("td");
    td4.textContent = person.to_pay.toLocaleString() + " Ft";
    row4.appendChild(td4);
  }
}