window.addEventListener("DOMContentLoaded", function(){
    let password = document.getElementById("signup_psw");
    password.addEventListener("input", function(){
        if (password.validity.tooLong || password.validity.tooShort || password.validity.valueMissing) {
            password.setCustomValidity("Password must be 7 characters or more consist of numbers, lower-case, and " +
                "upper-case letters.");
            password.reportValidity();
        } else {
            password.setCustomValidity("");
        }
    });
});