const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );

    $(".user-enabled").on("input", function (e){
        const user_id = e.target.parentNode.parentNode.id;
        const enabled = e.target.checked;
        console.log(user_id, enabled);
        $.ajax({
            type: "POST",
            url: ctx.ajaxUrl + "enable",
            contentType: "application/json",
            data: JSON.stringify({"id": user_id, "enable": enabled})
        }).done(function () {
            updateTable();
            successNoty("Saved");
        });
    });

});

