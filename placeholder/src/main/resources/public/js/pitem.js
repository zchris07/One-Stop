// function deleteEmployer(employerName) {
//     fetch('http://localhost:7000/main?taskName=' + taskName + '&duration_day=' +duration_day+ '&date='+ date, {
//         method: 'Delete',
//     }).then(res => window.location.reload = window.location.reload(true));
// }
// function  addPitem(taskName, duration_day, date) {
//     fetch('http://localhost:7000/main?taskName=' + taskName + '&duration_day=' +duration_day+ '&date='+ date, {
//         method: 'Post',
//     }).then(res => window.location.reload = window.location.reload(true));
// }

// let delButtons = document.querySelectorAll("li > button")
// Array.prototype.forEach.call(delButtons, function(button) {
//     button.addEventListener('click', deleteEmployer.bind(null, button.id));
// });
let currentList = null;
document.getElementById("submit_add_task").addEventListener('click', function () {
    if (currentList && validateTaskDuration()){
        taskName = document.getElementById("taskName").value;
        dueDay = document.getElementById("dueDay").value;
        date_string = document.getElementById("date").value;
        duration = document.getElementById("duration").value;
        importance = document.getElementById("importance").value;
        if (document.getElementById("flexible").checked) {
            flexible = "true"
        }
        else {
            isCheckedGrammar = "false"
        }


        console.log("clicked")
        console.log(taskName)
        console.log(dueDay)
        console.log(date_string)
        console.log(duration)
        console.log(importance)
        console.log(flexible)
        fetch('https://onestop-dev-ph.herokuapp.com/addTask?listId=' + currentList + '&taskName=' + taskName + '&dueDay=' +dueDay+ '&date='+ date_string
            + '&duration=' + duration + '&importance=' + importance + '&flexible=' + flexible, {
            method: 'Post',
        })
            .then(res => showTaskInList(currentList))
        ;}
}
);
document.getElementById("submit_add_list").addEventListener('click', function () {
        // if (validateTaskDuration()){
            listName = document.getElementById("listName").value;
            // userid = document.getElementById("userid").value;
            colabidstring = document.getElementById("colabidstring").value;
            console.log("clicked")
            console.log(colabidstring)
            // fetch('http://localhost:7000/addList?listName=' + listName + '&userid=' + userid + '&colabidstring=' + colabidstring, {
            //     method: 'Post',
            // });
            fetch('https://onestop-dev-ph.herokuapp.com/addList?listName=' + listName + '&colabidstring=' + colabidstring, {
                method: 'Post',
            });
                // .then(res => window.location.reload = window.location.reload(true))
            ;}
    // }
);

document.getElementById("submit_add_avail").addEventListener('click', function () {
        // if (validateTaskDuration()){
        weekstr = document.getElementById("weekcount").value;
        // userid = document.getElementById("userid").value;
        mondayAvail = document.getElementById("monday").value;
        tuesdayAvail = document.getElementById("tuesday").value;
        wednesdayAvail = document.getElementById("wednesday").value;
        thursdayAvail = document.getElementById("thursday").value;
        fridayAvail = document.getElementById("friday").value;
        saturdayAvail = document.getElementById("saturday").value;
        sundayAvail = document.getElementById("sunday").value;
        repeat = document.getElementById("repeat").value;

        console.log("clicked")
        console.log(weekstr)
        console.log(mondayAvail)
        console.log(tuesdayAvail)
        console.log(wednesdayAvail)
        console.log(thursdayAvail)
        console.log(fridayAvail)
        console.log(saturdayAvail)
        console.log(sundayAvail)
        console.log(repeat)

        // fetch('http://localhost:7000/addList?listName=' + listName + '&userid=' + userid + '&colabidstring=' + colabidstring, {
        //     method: 'Post',
        // });
        fetch('https://onestop-dev-ph.herokuapp.com/addAvail?weekstr=' + weekstr + '&mondayAvail=' + mondayAvail
            + '&tuesdayAvail=' + tuesdayAvail + '&wednesdayAvail=' + wednesdayAvail + '&thursdayAvail=' + thursdayAvail
            + '&fridayAvail=' + fridayAvail + '&saturdayAvail=' + saturdayAvail + '&sundayAvail=' + sundayAvail + '&repeat=' + repeat, {
            method: 'Post',
        });
        // .then(res => window.location.reload = window.location.reload(true))
    }
    // }
);

for (let i of document.querySelectorAll(".delete_task"))
{
    i.addEventListener('click', function (event) {
        taskName = event.target.parentElement.parentElement.firstElementChild.innerHTML
        console.log("clicked")
        console.log(taskName)
        // console.log(duration_day)
        // console.log(date_string)
        fetch('https://onestop-dev-ph.herokuapp.com/main?taskName=' + taskName , {
            method: 'Delete',
        }).then(res => window.location.reload = window.location.reload(true));
    })
}
for (let i of document.querySelectorAll(".delete_list"))
{
    i.addEventListener('click', function (event) {
        listId = event.target.parentElement.parentElement.firstElementChild.id.substring("taskList-id-".length);
        console.log("clicked")
        if (listId==="1"){
            alert("Sorry, you may not delete fist list");
            return;
        }
        console.log(listId)
        // console.log(duration_day)
        // console.log(date_string)
        fetch('https://onestop-dev-ph.herokuapp.com/deleteList?listId=' + listId , {
            method: 'Delete',
        }).then(res => window.location.reload = window.location.reload(true));
    })
}

function deleteTask(taskName) {
    console.log("clicked")
    console.log(taskName)
    console.log(currentList)
    // console.log(duration_day)
    // console.log(date_string)
    fetch('https://onestop-dev-ph.herokuapp.com/deleteTask?listId=' + currentList + '&taskName=' + taskName , {
        method: 'Delete',
    }).then(res => window.location.reload = window.location.reload(true));
}
// show details of task
function showDetail(taskName) {
    location.href = './showDetail?listId=' + currentList + '&taskName=' + taskName;
}

const showTaskInList = listId => {
    currentList = listId;
    if (currentList !== null) {
        console.log(`listId=${listId}`);
        fetch('https://onestop-dev-ph.herokuapp.com/showList?listId=' + listId)
            .then(res => res.json())
            .then(json => {
                document.getElementById('all-tasks').innerHTML = '';
                for (let task of json["taskList"]) {
                    let html = `<tr>
<td>${task['taskName']}</td>
<td>${task['duration_day']}</td>
<td>${task['date']} <button class="edit_task_date btn btn-fail" data-toggle="modal" data-target="#edited">
  Edit
</button>
<!-- Modal -->
<!--<div class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog" id="edit-modal"-->
<div class="modal fade" id="edited" tabindex="-1" role="dialog"  aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModal3Label">Edit task date</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <input type="date" id="editeddueDay" name="trip-start"
                                                       value="2021-10-19"
                                                       min="1901-01-01" max="2030-12-31" >
      </div>
      <div class="modal-footer">
         <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button> 
         <button type="button" class="btn btn-primary" onclick="editDate(taskName)">Save changes</button>
      </div>
      </div>
    </div>
  </div>
 </td>
  
<td>${task['duration']}</td>
<td>${task['importance']}</td>
<td>${task['exactStart']}</td>
<td>${task['exactEnd']}</td>
<td><button class="delete_task btn btn-fail" onclick="deleteTask('${task['taskName']}')" type="button" >Delete Task</button></td>
<!--show detail of a task-->
<td><button class="delete_task btn btn-fail" onclick="showDetail('${task['taskName']}')" type="button" >Task Detail</button></td>
</tr>
`
                    document.getElementById('all-tasks').innerHTML += html;
                }

                for (let btn of document.querySelectorAll('.edit_task_date')) {
                    btn.addEventListener('click', event => {
                        taskName = event.target.parentNode.parentNode.firstElementChild.innerHTML;
                    });
                }
            });
}
}

for (let i of document.querySelectorAll(".list-row")) {
    i.addEventListener('click', function (event) {
        let myId = event.target.id.substring("taskList-id-".length);
        showTaskInList(myId);
    });
}

window.addEventListener('DOMContentLoaded', e => {
    showTaskInList(currentList);
})

function validateTaskDuration() {
    const taskName = document.getElementById("taskName").value;
    if (taskName ==="" ) {
        alert("task name cannot be null!");
        return false;

    }
    return true;
//
}

function onclickInput(taskName) {
    return taskName;
}

function validateListName() {
    const listName = document.getElementById("listName").value;
    if (taskName ==="" ) {
        alert("list name cannot be null!");
        return false;

    }
    return true;
//
}

function editDate(taskName1){
    const date_string = document.getElementById("editeddueDay").value;
    // currentList
    console.log("clicked")
    console.log(taskName)
    console.log(currentList)
    console.log(date_string)
    fetch('https://onestop-dev-ph.herokuapp.com/editDate?listId=' + currentList + '&taskName=' + taskName+ '&editeddueDay=' + date_string , {
        method: 'Put',
    })
        .then(res => window.location.reload = window.location.reload(true));
}

document.getElementById("submit_add_list").addEventListener('click', function () {
        const listName = document.getElementById("listName").value;
        if (validateListName()){
            fetch('https://onestop-dev-ph.herokuapp.com/main?listNamee=' + listName , {
                method: 'Post',
            }).then(res => window.location.reload = window.location.reload(true));}
    }
);
