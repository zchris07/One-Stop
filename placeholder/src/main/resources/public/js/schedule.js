let today = new Date();
let currentMonth = today.getMonth();
let currentYear = today.getFullYear();
let selectYear = document.getElementById("year");
let selectMonth = document.getElementById("month");
let result = [];
// function get_date() {
// console.log("get dates list")

// }
let year = 2021;
let month = 12;
let monthAndYear = document.getElementById("monthAndYear");
let firstDay = (new Date(year, month)).getDay();
let daysInMonth = 32 - new Date(year, month, 32).getDate();
let months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

fetch(path+'schedule' , {
    method: 'put',
}).then(res => res.json())
    .then(json=> {
        // console.log(json);
        for (let task of json) {
            const result_of_task = task["date"].split("-");
            let current_date_day =Number(result_of_task[0]);
            let current_date_month =Number(result_of_task[1]);
            let current_date_year =Number(result_of_task[2]);

            // console.log(result);
            result.push([current_date_day,current_date_month,current_date_year,task['taskName'],task['exactStart'],task['exactEnd']]);

        }
        showCalendar(11,2021);
    });

let tbl = document.getElementById("calendar-body"); // body of the calendar

// clearing all previous cells
tbl.innerHTML = "";

// filing data about month and in the page via DOM.
monthAndYear.innerHTML = months[10] + " " + 2021;
// get_date();
// creating all cells
let date = 1;
for (let i = 0; i < 6; i++) {
    // creates a table row
    let row = document.createElement("tr");
    //creating individual cells, filing them up with data.
    for (let j = 0; j < 7; j++) {
        if (i === 0 && j < firstDay) {
            let cell = document.createElement("td");
            let cellText = document.createTextNode("");
            cell.appendChild(cellText);
            row.appendChild(cell);
        }
        else if (date > daysInMonth) {
            break;
        }

        else {
            let cell = document.createElement("td");
            let cellText = document.createTextNode(date);
            if (result.length === 0){
                console.log(result);
            }
            let to_return = ""
            let flag =0
            for (let date_in_result of result){
                if (Number(date_in_result[0]) === year && Number(date_in_result[1])-1===month &&Number(date_in_result[2])===date ){
                    // cell.classList.add("text-light");
                    to_return += "Your task:"+date_in_result[3] + " start time: " + date_in_result[4] + " end time: " + date_in_result[5]
                    flag =1

                }
            }
            // console.log(result);
            if(flag===1){
                cellText = document.createTextNode(to_return);
            }
            // console.log(to_return);

            if (date === today.getDate() && year === today.getFullYear() && month === today.getMonth()) {
                cell.classList.add("bg-info");
            } // color today's date
            cell.appendChild(cellText);
            row.appendChild(cell);
            date++;
        }


    }

    tbl.appendChild(row); // appending each row into calendar body.
}


// get_date();
// console.log(result);

showCalendar(currentMonth, currentYear);

next();
previous();
function next() {
    currentYear = (currentMonth === 11) ? currentYear + 1 : currentYear;
    currentMonth = (currentMonth + 1) % 12;
    showCalendar(currentMonth, currentYear);
}

function previous() {
    currentYear = (currentMonth === 0) ? currentYear - 1 : currentYear;
    currentMonth = (currentMonth === 0) ? 11 : currentMonth - 1;

    showCalendar(currentMonth, currentYear);
}

function jump() {
    currentYear = parseInt(selectYear.value);
    currentMonth = parseInt(selectMonth.value);
    // get_date();
    showCalendar(currentMonth, currentYear);
}

// document.addEventListener('DOMContentLoaded', () => {console.log(result);showCalendar(currentMonth, currentYear)});
function showCalendar(month, year) {
    console.log(result);
        let firstDay = (new Date(year, month)).getDay();
        let daysInMonth = 32 - new Date(year, month, 32).getDate();

        let tbl = document.getElementById("calendar-body"); // body of the calendar

        // clearing all previous cells
        tbl.innerHTML = "";

        // filing data about month and in the page via DOM.
        monthAndYear.innerHTML = months[month] + " " + year;
        selectYear.value = year;
        selectMonth.value = month;
        // get_date();
        // creating all cells
        let date = 1;
        for (let i = 0; i < 6; i++) {
            // creates a table row
            let row = document.createElement("tr");
            //creating individual cells, filing them up with data.
            for (let j = 0; j < 7; j++) {
                if (i === 0 && j < firstDay) {
                    let cell = document.createElement("td");
                    let cellText = document.createTextNode("");
                    cell.appendChild(cellText);
                    row.appendChild(cell);
                }
                else if (date > daysInMonth) {
                    break;
                }

                else {
                    let cell = document.createElement("td");
                    let cellText = document.createTextNode(date);
                    if (result.length === 0){
                        console.log(result);
                    }
                    let to_return = ""
                    let flag =0
                    cell.appendChild(cellText);
                    for (let date_in_result of result){
                        if (Number(date_in_result[0]) === year && Number(date_in_result[1])-1===month &&Number(date_in_result[2])===date ){
                            cell.appendChild(document.createElement('br'));
                            to_return = " Your task: "+date_in_result[3] +" start time: "+date_in_result[4] +
                                " end time: "+ date_in_result[5]
                            cellText = document.createTextNode(to_return)
                            cell.appendChild(cellText);
                            cell.appendChild(document.createElement('br'));
                        }
                    }
                    // console.log(result);
                    // if(flag===1){
                    //     cellText = document.createTextNode(to_return);
                    // }
                    if (date === today.getDate() && year === today.getFullYear() && month === today.getMonth()) {
                        cell.classList.add("bg-info");
                    } // color today's date
                    row.appendChild(cell);
                    date++;
                }


            }

            tbl.appendChild(row); // appending each row into calendar body.
        }

}// document.addEventListener('DOMContentLoaded', () => {console.log(result);showCalendar(currentMonth, currentYear)});