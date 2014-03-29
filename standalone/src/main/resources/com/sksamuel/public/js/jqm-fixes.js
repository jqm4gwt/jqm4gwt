// jqm 1.4.2 tabs widget fix: https://github.com/jquery/jquery-mobile/issues/7169
$.widget( "ui.tabs", $.ui.tabs, {

    _createWidget: function( options, element ) {
        var page, delayedCreate,
            that = this;

        if ( $.mobile.page ) {
            page = $( element )
                .parents( ":jqmData(role='page'),:mobile-page" )
                .first();

            if ( page.length > 0 && !page.hasClass( "ui-page-active" ) ) {
                delayedCreate = this._super;
                page.one( "pagebeforeshow", function() {
                    delayedCreate.call( that, options, element );
                });
            }
        } else {
            return this._super();
        }
    }
});

// jqm 1.4.2 tabs widget heightStyle="auto" fix: https://github.com/jquery/jquery-mobile/issues/6392
$.widget( "ui.tabs", $.ui.tabs, {
	_setupHeightStyle: function( heightStyle ) {
		if ( heightStyle === "auto" ) {
			maxHeight = 0;
			this.panels.each(function() {
				maxHeight = Math.max( maxHeight, $( this ).height( "" ).actual( 'height' ) );
			}).height( maxHeight );
		} else {
            this._super();
        }
	}
});
