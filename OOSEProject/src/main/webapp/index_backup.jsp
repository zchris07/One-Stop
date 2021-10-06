<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<!-- to make responsive -->
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<!-- include bootstrap css -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/css/bootstrap.min.css" />

<!-- include jquery and bootstrap js -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/js/bootstrap.min.js"></script>

<!-- include sweetalert for displaying dialog messages -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/2.1.2/sweetalert.min.js"></script>

<div class="container">
    <div class="row">
        <div class="col-sm">
            <div class="row" style="margin-top: 50px;">
                <div class="col-md-12">
                    <div class="input-group">
<%--                        <form method="POST" onsubmit="return taskObj.deleteProject(this);" style="display: contents;"--%>
                            <input type="text" class="form-control" placeholder="List Name" aria-label="List Name" aria-describedby="basic-addon2">
                            <div class="input-group-append">
                                <button class="btn btn-outline-success" type="button">Create List</button>
                                <button class="btn btn-outline-danger" type="button">Delete List</button>
                            </div>
<%--                        </form>--%>
                    </div>
<%--                    <button type="button" class="btn btn-outline-success" data-toggle="modal" data-target="#addTaskModal">Create List</button>--%>


<%--                    <form method="POST" onsubmit="return taskObj.deleteProject(this);" style="display: contents;">--%>
<%--                        <select name="project" class="form-control" style="display: initial; width: 200px; margin-left: 5px; margin-right: 5px;" id="form-task-hour-calculator-all-projects"></select>--%>
<%--                        <input type="submit" class="btn btn-outline-danger" value="Delete List">--%>
<%--                    </form>--%>
                </div>
            </div>
            <div class="container" style="margin-top: 50px; margin-bottom: 50px;">
                <table class="table table-hover">
                    <tbody>
                    <tr style="transform: rotate(0);">
                        <th scope="row"><a href="#" class="stretched-link">1</a></th>
                        <td>List 1</td>
                    </tr>
                    <tr>
                        <th scope="row">2</th>
                        <td>List 2</td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td>List 3</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="col-sm">
            <div class="container" style="margin-top: 50px; margin-bottom: 50px;">

                <!-- button to add task -->
                <div class="row" style="margin-bottom: 50px;">
                    <div class="col-md-12">
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="Task" aria-label="Task" aria-describedby="basic-addon2">
                            <div class="input-group-append">
                                <button class="btn btn-outline-success" type="button">Add Task</button>
                                <button class="btn btn-outline-danger" type="button">Delete Task</button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- show all tasks -->
                <table class="table">
                    <caption class="text-center">All Tasks</caption>
                    <tr>
                        <th>Task</th>
                        <th>Project</th>
                        <th>Duration</th>
                        <th>Date</th>
                    </tr>

                    <tbody id="all-tasks"></tbody>
                </table>
            </div>

            <!-- modal to add project and task -->
            <div class="modal fade" id="addTaskModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Add Task</h5>
                            <button class="close" type="button" data-dismiss="modal">x</button>
                        </div>

                        <div class="modal-body">
                            <form method="POST" onsubmit="return taskObj.addTask(this);" id="form-task-hour-calculator">

                                <!-- select project from already created -->
                                <div class="form-group">
                                    <label>Project</label>
                                    <select name="project" id="add-task-project" class="form-control" required></select>
                                </div>

                                <!-- create new project -->
                                <div class="form-group">
                                    <label>New Project</label>
                                    <input type="text" name="new_project" id="add-project" class="form-control" placeholder="Project Name">

                                    <button type="button" onclick="taskObj.addProject();" class="btn btn-primary" style="margin-top: 10px;">Add Project</button>
                                </div>

                                <!-- enter task -->
                                <div class="form-group">
                                    <label>Task</label>
                                    <input type="text" name="task" class="form-control" placeholder="What are you going to do ?" required />
                                </div>
                            </form>
                        </div>

                        <!-- form submit button -->
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button type="submit" form="form-task-hour-calculator" class="btn btn-primary">Add Task</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-sm">
            <div class="container" style="margin-top: 125px; margin-bottom: 50px;">
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault">
                    <label class="form-check-label" for="flexSwitchCheckDefault">List Functional Option</label>
                </div>
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" >
                    <label class="form-check-label" for="flexSwitchCheckDefault">List Functional Option</label>
                </div>
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" >
                    <label class="form-check-label" for="flexSwitchCheckDefault">List Functional Option</label>
                </div>
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="flexSwitchCheckDefault" >
                    <label class="form-check-label" for="flexSwitchCheckDefault">List Functional Option</label>
                </div>
            </div>
        </div>
    </div>
</div>


<style>
    /* style when project is started */
    .started {
        color: white;
        font-weight: bold;
        background: green;
        padding: 5px;
        border-radius: 5px;
    }

    /* style when project is completed */
    .completed {
        color: white;
        font-weight: bold;
        background: greenyellow;
        padding: 5px;
        border-radius: 5px;
    }
</style>