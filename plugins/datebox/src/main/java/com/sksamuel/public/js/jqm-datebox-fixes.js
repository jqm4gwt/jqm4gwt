(function() {

    $.widget("mobile.datebox", $.mobile.datebox, {
        setGridDateFormatter: function( formatter ) {
            // Provide a PUBLIC function to set the date buttons formatter
            // ACCEPTS: function with (input: array of Date; output: array of String/Html)
            if ( typeof formatter === "function" ) {
                this.gridDateFormatter = formatter;
                this.refresh();
            }
        }
    });

    var _prev = $.mobile.datebox.prototype._build.calbox;
    $.mobile.datebox.prototype._build.calbox = function() {
        _prev.call(this);
        if (typeof this.gridDateFormatter === "undefined" || this.gridDateFormatter === null) return;

        var dateBtns = this.d.intHTML.find(".ui-datebox-griddate.ui-btn");
        if (dateBtns.length === 0) return;

        // -1 previous year's Dec
        // 12 next year's Jan
        // 0..11 current year's Jan..Dec

        var thisYY = this.theDate.getFullYear(); // this.theDate is current header's date
        var dates = [];
        dateBtns.each(function( index, elt ) { // elt == this
            var mm = $(elt).data("month");
            var dd = $(elt).data("date");
            var yy = thisYY;
            if (mm < 0) {
                yy--;
                mm = 12 + mm;
            } else if (mm > 11) {
                yy++;
                mm = mm - 12;
            };
            var d = new Date(yy, mm, dd, 0, 0, 0);
            dates.push(d);
        });
        var fmtDates = this.gridDateFormatter(dates);
        if ($.isArray(fmtDates) && fmtDates.length === dates.length) {
            dateBtns.each(function( index, elt ) { // elt == this
                $(elt).html(fmtDates[index]);
            });
        }
    };
})();
