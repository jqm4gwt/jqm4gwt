// jqm 1.4.5 tabs widget fix: https://github.com/jquery/jquery-mobile/issues/7169
$.widget( "ui.tabs", $.ui.tabs, {

    _isLocal: function( anchor ) {
        var path, baseUrl, absUrl;

        if ( $.mobile.ajaxEnabled ) {
            path = $.mobile.path;
            baseUrl = path.parseUrl( $.mobile.base.element.attr( "href" ) );
            absUrl = path.parseUrl( path.makeUrlAbsolute( anchor.getAttribute( "href" ),
                baseUrl ) );

            return ( path.isSameDomain( absUrl.href, baseUrl.href ) &&
                absUrl.pathname === baseUrl.pathname );
        }

        var rhash = /#.*$/;
        var anchorUrl, locationUrl;

        anchorUrl = anchor.href.replace( rhash, "" );
        locationUrl = location.href.replace( rhash, "" );

        // decoding may throw an error if the URL isn't UTF-8 (#9518)
        try {
            anchorUrl = decodeURIComponent( anchorUrl );
        } catch ( error ) {}
        try {
            locationUrl = decodeURIComponent( locationUrl );
        } catch ( error ) {}

        return anchor.hash.length > 1 && anchorUrl === locationUrl;
    },

    _processTabs: function() {
        var that = this;

        this.tablist = this._getList()
            .addClass( "ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all" )
            .attr( "role", "tablist" );

        this.tabs = this.tablist.find( "> li:has(a[href])" )
            .addClass( "ui-state-default ui-corner-top" )
            .attr({
                role: "tab",
                tabIndex: -1
            });

        this.anchors = this.tabs.map(function() {
                return $( "a", this )[ 0 ];
            })
            .addClass( "ui-tabs-anchor" )
            .attr({
                role: "presentation",
                tabIndex: -1
            });

        this.panels = $();

        this.anchors.each(function( i, anchor ) {
            var selector, panel, panelId,
                anchorId = $( anchor ).uniqueId().attr( "id" ),
                tab = $( anchor ).closest( "li" ),
                originalAriaControls = tab.attr( "aria-controls" );

            // inline tab
            if ( that._isLocal( anchor ) ) {
                selector = anchor.hash;
                panel = that.element.find( that._sanitizeSelector( selector ) );
            // remote tab
            } else {
                panelId = that._tabId( tab );
                selector = "#" + panelId;
                panel = that.element.find( selector );
                if ( !panel.length ) {
                    panel = that._createPanel( panelId );
                    panel.insertAfter( that.panels[ i - 1 ] || that.tablist );
                }
                panel.attr( "aria-live", "polite" );
            }

            if ( panel.length) {
                that.panels = that.panels.add( panel );
            }
            if ( originalAriaControls ) {
                tab.data( "ui-tabs-aria-controls", originalAriaControls );
            }
            tab.attr({
                "aria-controls": selector.substring( 1 ),
                "aria-labelledby": anchorId
            });
            panel.attr( "aria-labelledby", anchorId );
        });

        this.panels
            .addClass( "ui-tabs-panel ui-widget-content ui-corner-bottom" )
            .attr( "role", "tabpanel" );
    },

    load: function( index, event ) {
        index = this._getIndex( index );
        var that = this,
            tab = this.tabs.eq( index ),
            anchor = tab.find( ".ui-tabs-anchor" ),
            panel = this._getPanelForTab( tab ),
            eventData = {
                tab: tab,
                panel: panel
            };

        // not remote
        if ( that._isLocal( anchor[ 0 ] ) ) {
            return;
        }

        this.xhr = $.ajax( this._ajaxSettings( anchor, event, eventData ) );

        // support: jQuery <1.8
        // jQuery <1.8 returns false if the request is canceled in beforeSend,
        // but as of 1.8, $.ajax() always returns a jqXHR object.
        if ( this.xhr && this.xhr.statusText !== "canceled" ) {
            tab.addClass( "ui-tabs-loading" );
            panel.attr( "aria-busy", "true" );

            this.xhr
                .success(function( response ) {
                    // support: jQuery <1.8
                    // http://bugs.jquery.com/ticket/11778
                    setTimeout(function() {
                        panel.html( response );
                        that._trigger( "load", event, eventData );
                    }, 1 );
                })
                .complete(function( jqXHR, status ) {
                    // support: jQuery <1.8
                    // http://bugs.jquery.com/ticket/11778
                    setTimeout(function() {
                        if ( status === "abort" ) {
                            that.panels.stop( false, true );
                        }

                        tab.removeClass( "ui-tabs-loading" );
                        panel.removeAttr( "aria-busy" );

                        if ( jqXHR === that.xhr ) {
                            delete that.xhr;
                        }
                    }, 1 );
                });
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

// Panel: Retain current page during closing process
// See https://github.com/jquery/jquery-mobile/commit/827292ededd3b6b0d3fef737fda6b95774760228
$.widget( "mobile.panel", $.mobile.panel, {
    close: function( immediate ) {
        if ( this._open ) {
            var self = this,

                // Record what the page is the moment the process of closing begins, because it
                // may change by the time the process completes
                currentPage = self._page(),
                o = this.options,

                _closePanel = function() {

                    self.element.removeClass( o.classes.panelOpen );

                    if ( o.display !== "overlay" ) {
                        self._wrapper.removeClass( self._pageContentOpenClasses );
                        self._fixedToolbars().removeClass( self._pageContentOpenClasses );
                    }

                    if ( !immediate && $.support.cssTransform3d && !!o.animate ) {
                        ( self._wrapper || self.element )
                            .animationComplete( complete, "transition" );
                    } else {
                        setTimeout( complete, 0 );
                    }

                    if ( self._modal ) {
                        self._modal
                            .removeClass( self._modalOpenClasses )
                            .height( "" );
                    }
                },
                complete = function() {
                    if ( o.theme && o.display !== "overlay" ) {
                        currentPage.parent().removeClass( o.classes.pageContainer + "-themed " +
                            o.classes.pageContainer + "-" + o.theme );
                    }

                    self.element.addClass( o.classes.panelClosed );

                    if ( o.display !== "overlay" ) {
                        currentPage.parent().removeClass( o.classes.pageContainer );
                        self._wrapper.removeClass( o.classes.pageContentPrefix + "-open" );
                        self._fixedToolbars().removeClass( o.classes.pageContentPrefix + "-open" );
                    }

                    if ( $.support.cssTransform3d && !!o.animate && o.display !== "overlay" ) {
                        self._wrapper.removeClass( o.classes.animate );
                        self._fixedToolbars().removeClass( o.classes.animate );
                    }

                    self._fixPanel();
                    self._unbindFixListener();
                    $.mobile.resetActivePageHeight();

                    currentPage.jqmRemoveData( "panel" );

                    self._trigger( "close" );

                    self._openedPage = null;
                };

            self._trigger( "beforeclose" );

            _closePanel();

            self._open = false;
        }
    }
});
