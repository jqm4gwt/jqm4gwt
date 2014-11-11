// jqm 1.4.5 tabs widget fix: https://github.com/jquery/jquery-mobile/issues/7169
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
            return this._super( options, element );
        }
    }
});

// jqm 1.4.5 tabs widget heightStyle="auto" fix: https://github.com/jquery/jquery-mobile/issues/6392
$.widget( "ui.tabs", $.ui.tabs, {
    _setupHeightStyle: function( heightStyle ) {
        var maxHeight,
        parent = this.element.parent();

        if ( heightStyle === "auto" ) {
            maxHeight = 0;
            this.panels.each(function() {
                maxHeight = Math.max( maxHeight, $( this ).height( "" ).actual( 'height' ) );
            }).height( maxHeight );
        } else if ( heightStyle === "fill" ) {
            maxHeight = parent.actual( 'height' );
            maxHeight -= this.element.actual( 'outerHeight' ) - this.element.actual( 'height' );

            this.element.siblings( ":visible" ).each(function() {
                var elem = $( this ),
                    position = elem.css( "position" );

                if ( position === "absolute" || position === "fixed" ) {
                    return;
                }
                maxHeight -= elem.actual( 'outerHeight', { includeMargin : true } );
            });

            this.element.children().not( this.panels ).each(function() {
                maxHeight -= $( this ).actual( 'outerHeight', { includeMargin : true } );
            });

            this.panels.each(function() {
                $( this ).height( Math.max( 0, maxHeight -
                    $( this ).actual( 'innerHeight') + $( this ).actual( 'height') ) );
            })
            .css( "overflow", "auto" );
        } else {
            return this._super( heightStyle );
        }
    }
});

// See https://github.com/jquery/jquery-mobile/issues/7579
$.widget( "mobile.popup", $.mobile.popup, {
    _eatEventAndClose: function( theEvent ) {
        if ( this.options.dismissible ) {
            var doClick = this.element.attr( "data-" + $.mobile.ns + "closeThenClick" );
            if ( doClick === "true" ) {
                var x = theEvent.clientX;
                var y = theEvent.clientY;

                theEvent.preventDefault();
                theEvent.stopImmediatePropagation();
                this.close();

                var newTarget = document.elementFromPoint( x, y );
                if (newTarget != null) {
                    setTimeout(function() {
                        newTarget.focus();
                        newTarget.click();
                    }, 200); // Win 8.1 is not OK with timeout less than 200
                }
                return false;
            }
        }
        return this._super( theEvent );
    }
});

// See https://github.com/jquery/jquery-mobile/issues/7830
$.widget( "mobile.filterable", $.mobile.filterable, {
    _onKeyPress: function( event ) {
        if ( this._preventKeyPress ) {
            if ( event.keyCode === $.ui.keyCode.ENTER ) event.preventDefault();
            this._preventKeyPress = false;
        } else {
            this._super( event );
        }
    }
});
