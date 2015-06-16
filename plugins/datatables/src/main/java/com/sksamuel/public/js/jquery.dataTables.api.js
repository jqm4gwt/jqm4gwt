// https://www.datatables.net/plug-ins/api/order.neutral()
$.fn.dataTable.Api.register( 'order.neutral()', function () {
    return this.iterator( 'table', function ( s ) {
        s.aaSorting.length = 0;
        s.aiDisplay.sort( function (a,b) {
            return a-b;
        } );
        s.aiDisplayMaster.sort( function (a,b) {
            return a-b;
        } );
    } );
} );

//http://datatables.net/plug-ins/api/column().title()
$.fn.dataTable.Api.register( 'column().title()', function () {
    var colheader = this.header();
    return $(colheader).text().trim();
} );

// http://datatables.net/plug-ins/api/page.jumpToData()
$.fn.dataTable.Api.register( 'page.jumpToData()', function ( data, column ) {
    var pos = this.column(column, {order:'current'}).data().indexOf( data );

    if ( pos >= 0 ) {
        var page = Math.floor( pos / this.page.info().length );
        this.page( page ).draw( false );
    }

    return this;
} );

// http://datatables.net/plug-ins/api/row().show()
$.fn.dataTable.Api.register('row().show()', function() {
    var page_info = this.table().page.info();
    // Get row index
    var new_row_index = this.index();
    // Row position
    var row_position = this.table().rows()[0].indexOf( new_row_index );
    // Already on right page ?
    if( row_position >= page_info.start && row_position < page_info.end ) {
        // Return row object
        return this;
    }
    // Find page number
    var page_to_display = Math.floor( row_position / this.table().page.len() );
    // Go to that page
    this.table().page( page_to_display );
    // Return row object
    return this;
});
