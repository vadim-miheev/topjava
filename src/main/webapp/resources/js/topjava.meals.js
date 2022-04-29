const mealsAjaxUrl = "meals/ui/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
                    "desc"
                ]
            ]
        })
    );

    $(".datepicker").datetimepicker({
        timepicker:false,
        format:'Y-m-d'
    });

    $(".timepicker").datetimepicker({
        datepicker:false,
        format:'H:i'
    });

});

function updateFilter() {
    $.get(ctx.ajaxUrl + "?" + $("#filter-form").serialize()).done(function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
        successNoty("Updated");
    });
}

function clearFilter() {
    $("#filter-form input").val("");
    updateTable();
}