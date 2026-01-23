function ActionBtnONB() { }
var style = document.createElement('style');

style.innerHTML = `
/* VIEW - Blue */
.viewBtn {
    background-color: #1e88e5 !important;
    border-color: #1e88e5 !important;
    color: #fff !important;
}
.viewBtn:hover {
    background-color: #1565c0 !important;
}

/* EDIT - Green */
.editBtn {
    background-color: #43a047 !important;
    border-color: #43a047 !important;
    color: #fff !important;
}
.editBtn:hover {
    background-color: #2e7d32 !important;
}

/* DELETE - Black */
.deleteBtn {
    background-color: #f50f0f !important;
    border-color: #f50f0f !important;
    color: #fff !important;
}
.deleteBtn:hover {
    background-color: #b52d2d !important;
}

/* DISABLED - Grey */
.viewBtn:disabled,
.editBtn:disabled,
.deleteBtn:disabled {
    background-color: #9e9e9e !important;
    border-color: #9e9e9e !important;
    color: #e0e0e0 !important;
    cursor: not-allowed !important;
    opacity: 0.6 !important;
}

.viewBtn:disabled:hover,
.editBtn:disabled:hover,
.deleteBtn:disabled:hover {
    background-color: #9e9e9e !important;
}

.viewBtn:focus,
.editBtn:focus,
.deleteBtn:focus {
    box-shadow: none !important;
    outline: none !important;
}

.viewBtn,
.viewBtn:hover,
.viewBtn:focus,
.viewBtn:disabled,
.editBtn,
.editBtn:hover,
.editBtn:focus,
.editBtn:disabled,
.deleteBtn,
.deleteBtn:hover,
.deleteBtn:focus,
.deleteBtn:disabled {
    border-radius: 20px !important;
}
`;


document.head.appendChild(style);

ActionBtnONB.prototype.init = function (params) {
    this.span = document.createElement('span');

    if (params.data) {
        var data = params.data;
        this.rk = data.rk;
        this.editRendered = data.editRenderONB;
        this.editDisabled = data.editDisabledONB;
    }
};

ActionBtnONB.prototype.getGui = function () {

    // VIEW
    var viewBtn = document.createElement('button');
    viewBtn.type = "button";
    viewBtn.title = this.editDisabled ? "Inactive - Cannot View" : "View";
    viewBtn.className =
        "ui-button ui-widget ui-state-default  ui-button-icon-only viewBtn";
    viewBtn.disabled = this.editDisabled;
    if (!this.editDisabled) {
        viewBtn.onclick = () =>
            rc_ViewClicked([{ name: 'rowIndex', value: this.rk }]);
    }

    viewBtn.innerHTML =
        '<span class="ui-button-icon-left ui-icon ui-c fa-landPg-eye"></span>';
    this.span.appendChild(viewBtn);

    if (this.editRendered === true) {

        // EDIT
        var editBtn = document.createElement('button');
        editBtn.type = "button";
        editBtn.title = this.editDisabled ? "Inactive - Cannot Edit" : "Edit";
        editBtn.className =
            "ui-button ui-widget ui-state-default  ui-button-icon-only editBtn";
        editBtn.disabled = this.editDisabled;
        if (!this.editDisabled) {
            editBtn.onclick = () =>
                rc_editClicked([{ name: 'rowIndex', value: this.rk }]);
        }

        editBtn.innerHTML =
            '<i class="fa fa-edit" style="font-size:14px;color:white;margin-left:-6px;text-align:center;" ></i>';

        this.span.appendChild(editBtn);

        // DELETE
        var delBtn = document.createElement('button');
        delBtn.type = "button";
        delBtn.title = this.editDisabled ? "Inactive - Cannot Delete" : "Delete";
        delBtn.className =
            "ui-button ui-widget ui-state-default  ui-button-icon-only deleteBtn";
        delBtn.disabled = this.editDisabled;
        if (!this.editDisabled) {
            delBtn.onclick = () =>
                rc_trashClicked([{ name: 'rowIndex', value: this.rk }]);
        }

        delBtn.innerHTML =
            '<span class="ui-button-icon-left ui-icon ui-c fa fa-trash"></span>';
        this.span.appendChild(delBtn);
    }

    return this.span;
};

svmdt.addCellRenderer('ONB', 'ActionBtnONB');
