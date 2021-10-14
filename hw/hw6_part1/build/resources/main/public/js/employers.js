function deleteEmployer(employerName) {
    fetch('http://localhost:7000/employers?name=' + employerName, {
            method: 'Delete',
        }
    ).then(res => window.location.reload = window.location.reload(true));
}
function  addEmployer(employerName, sector, summary) {
    fetch('http://localhost:7000/employers?name=' + employerName + '&sector=' + sector + '&sumamry=' + summary, {
        method: 'Post',
    }).then(res => window.location.reload = window.location.reload(true));
}

let delButtons = document.querySelectorAll("li > button")
Array.prototype.forEach.call(delButtons, function(button) {
    button.addEventListener('click', deleteEmployer.bind(null, button.id));
});

document.getElementById("add_btn").addEventListener('event', function () {
    name = document.getElementById("name").value;
    sector = document.getElementById("sector").value;
    summary = document.getElementById("summary").value;
    console.log("clicked")
    console.log(name)
    console.log(sector)
    console.log(summary)
    fetch('http://localhost:7000/employers?name=' + name + '&sector=' + sector + '&summary=' + summary, {
        method: 'Post',
    }).then(res => window.location.reload = window.location.reload(true));
});

function validateEmployee() {
    const name = document.getElementById("name");
    if (name.value.length < 2 || name.value.length > 150 || name.value.includes("$")||name.value.includes("@")||name.value.includes("^")||name.value.includes("%")||name.value.includes("~")) {
        alert("Employer name cannot be less than 2 characters, more than 150 characters, or include $, @, ^, %, ~!");
        return false;

    }
    const sector = document.getElementById("sector");
    if (sector.value.length < 2 || sector.value.length > 100 || sector.value.includes("$")||sector.value.includes("@")||sector.value.includes("^")||sector.value.includes("%")||sector.value.includes("~")) {
        alert("Employer sector cannot be less than 2 characters, more than 100 characters, or include any digits or $, @, ^, %, ~");
        return false
    } else {
        let event = new CustomEvent("event", {});
        document.dispatchEvent(event);
    }
}

