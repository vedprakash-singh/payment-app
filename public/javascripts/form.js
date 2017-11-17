function validateForm() {

var xx = document.forms["myForm"]["amount"].value;
    if (xx == "") {
        alert(" Please Enter Amount");
        return false;
    }
    var x = document.forms["myForm"]["name"].value;
    if (x == "") {
        alert(" Please Enter card holder name");
        return false;
    }
     var x1 = document.forms["myForm"]["cvvNo"].value;
        if (isNaN(x1)) {
            alert(" Please Enter Cvv No");
            return false;
        }
        var x2 = document.forms["myForm"]["cardNo"].value;
                if (isNaN(x2)) {
                    alert(" Please Enter card No");
                    return false;
                }
}