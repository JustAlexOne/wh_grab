! function(e) {
    "use strict";
    e.burger = {
        init: function() {
            $(".js-burger-menu").click(function() {
                $(this).toggleClass("open"), $(".js-mobile-menu").toggleClass("active")
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e) {
    "use strict";

    function t(e) {
        var t = /^\d+\./g,
            n = /\.(.+)/g;
        return null !== t.exec(e) && (e = n.exec(e)[0]), e = e.replace(/^\./, "").replace(/&#8217;/g, "'").replace(/&#8211;/g, "-").replace(/&#8216;/g, "'")
    }
    e.library = {
        app: angular.module("cardLibrary", ["checklist-model"]).factory("ApiClient", ["$filter", "$http", "$q", function(e, n, a) {
            var r = function(e) {
                var t = {};
                return angular.forEach(e, function(e) {
                    var n = {
                        id: e.id,
                        name: e.name,
                        slug: e.slug
                    };
                    e.hasOwnProperty("acf") && e.acf && e.acf.hasOwnProperty("icon") && (n.icon = {
                        src: e.acf.icon.url,
                        alt: e.acf.icon.alt
                    }), t[n.id] = n
                }), t
            };
            return {
                getFilters: function(e) {
                    return a.all({
                        factions: n({
                            url: "/wp-json/wp/v2/warbands?ver=10&per_page=100&lang=" + e,
                            method: "GET"
                        }).then(function(e) {
                            return r(e.data)
                        }),
                        types: n({
                            url: "/wp-json/wp/v2/card_types?ver=11&per_page=100&lang=" + e,
                            method: "GET"
                        }).then(function(e) {
                            return r(e.data)
                        }),
                        locations: n({
                            url: "/wp-json/wp/v2/sets?ver=12&per_page=100&lang=" + e,
                            method: "GET"
                        }).then(function(e) {
                            return r(e.data)
                        })
                    }).then(function(e) {
                        return e
                    })
                },
                getCards: function(e, a) {
                    return n({
                        url: "/wp-json/wp/v2/cards?ver=13&per_page=" + e + "&lang=" + a,
                        method: "GET"
                    }).then(function(e) {
                        var n = [];
                        return angular.forEach(e.data, function(e) {
                            var a = "",
                                r = "";
                            e.acf && (a = e.acf.card_number, r = e.acf.is_new);
                            var s = {
                                id: e.id,
                                name: t(e.title.rendered),
                                number: a,
                                type: null,
                                set: null,
                                faction: null,
                                isVisible: !0,
                                isNew: r
                            };
                            e.card_types.length && (s.type = e.card_types[0]), e.warbands.length && (s.faction = e.warbands[0]), e.sets.length && (s.set = e.sets[0]), e.acf && e.acf.card_image ? s.image = e.acf.card_image : s.image = "", n.push(s)
                        }), n
                    })
                }
            }
        }]).controller("CardDeckController", ["$scope", "ApiClient", function(e, t) {
            function n(t) {
                g = t == f && "+" === g ? "-" : "+", f = t, e.orderByItem = f, e.orderByDirection = g, e.orderBy = g + f
            }

            function a() {
                var t, n = e.cards,
                    a = 0;
                p.pages = [], p.currentPage = 0;
                for (var s = 0; s < n.length; s++) {
                    var i = n[s];
                    i.isVisible && (a += 1), i.isNew && (e.hasNew = !0)
                }
                t = Math.ceil(a / d);
                for (var o = 0; o < t; o++) p.pages.push({
                    pageNumber: o + 1
                });
                p.numberOfPages = t, r()
            }

            function r() {
                p.bottomIndex = p.currentPage * d, p.topIndex = p.currentPage * d + d, p.readableCurrentPageNumber = p.currentPage + 1, p.maxIndex = p.numberOfPages - 1
            }

            function s() {
                0 !== p.currentPage && (p.currentPage -= 1, r())
            }

            function i() {
                p.currentPage !== p.maxIndex && (p.currentPage += 1, r())
            }

            function o(e) {
                p.currentPage = e, r()
            }

            function c(t) {
                for (var n = 0; n < e.cards.length; n++) {
                    e.cards[n] !== t && (e.cards[n].showImage = !1)
                }
            }

            function l(e, t) {
                e.stopPropagation(), c(t), t.showImage ? t.showImage = !1 : t.showImage = !0
            }
            var d = 20,
                u = {
                    newSelected: !1,
                    factionCheckBoxesSelected: 0,
                    typeCheckBoxesSelected: 0,
                    locationCheckBoxesSelected: 0
                },
                p = {
                    pages: [],
                    bottomIndex: 0,
                    topIndex: 1,
                    currentPage: 0,
                    numberOfPages: 0,
                    readableCurrentPageNumber: 1,
                    maxIndex: 0
                },
                f = "number",
                g = "+";
            e.checkBoxesInfo = u, e.pagination = p, e.orderByDirection = g, e.orderByItem = f, e.orderBy = g + f, e.hasNew = !1, e.closeCardImages = c, e.toggleCardImage = l, e.prevPage = s, e.nextPage = i, e.setPage = o, e.changeOrderBy = n, e.checkCheckboxChange = function(t, n) {
                t ? u[n] += 1 : u[n] -= 1;
                for (var r = 0; r < e.cards.length; r++) {
                    var s = e.cards[r],
                        i = s.faction,
                        o = s.type,
                        c = s.set;
                    s.isHidden = u.newSelected && !s.isNew || u.factionCheckBoxesSelected && !e.filters.factions[i].isSelected || u.typeCheckBoxesSelected && !e.filters.types[o].isSelected || u.locationCheckBoxesSelected && !e.filters.locations[c].isSelected, s.isVisible = !s.isHidden
                }
                a()
            }, e.form = [], e.cards = [], t.getFilters($("#selectedLang").val()).then(function(n) {
                e.filters = n, t.getCards(1e3, $("#selectedLang").val()).then(function(t) {
                    e.cards = t, a()
                })
            })
        }])
    }
}(window.shade = window.shade || {}, angular),
function(e) {
    "use strict";

    function t(e) {
        var t = /^\d+\./g,
            n = /\.(.+)/g;
        return null !== t.exec(e) && (e = n.exec(e)[0]), e = e.replace(/^\./, "").replace(/&#8217;/g, "'").replace(/&#8211;/g, "-").replace(/&#8216;/g, "'")
    }
    var n;
    e.guide = {
        app: angular.module("deckGuide", ["checklist-model", "ngSanitize"]).factory("ApiClient", ["$filter", "$http", "$q", function(e, n, a) {
            var r = function(e, t) {
                var n = {};
                return angular.forEach(e, function(e, t) {
                    var a = {
                        id: e.id,
                        name: e.name,
                        slug: e.slug
                    };
                    e.hasOwnProperty("acf") && (e.acf.hasOwnProperty("icon") && (a.icon = {
                        src: e.acf.icon.url,
                        alt: e.acf.icon.alt
                    }), e.acf.hasOwnProperty("type") && (a.type = e.acf.type), e.acf.hasOwnProperty("colour") && (a.colour = e.acf.colour), e.acf.hasOwnProperty("image") && e.acf.image.hasOwnProperty("sizes") && (a.image = {
                        src: e.acf.image.sizes.medium,
                        alt: e.acf.image.alt
                    })), n[a.id] = a
                }), n
            };
            return {
                getData: function(e) {
                    return a.all({
                        factions: n({
                            url: "/wp-json/wp/v2/warbands?per_page=100&lang=" + e,
                            method: "GET"
                        }).then(function(e) {
                            return r(e.data)
                        }),
                        types: n({
                            url: "/wp-json/wp/v2/card_types?per_page=100&lang=" + e,
                            method: "GET"
                        }).then(function(e) {
                            return r(e.data)
                        }),
                        locations: n({
                            url: "/wp-json/wp/v2/sets?per_page=100&lang=" + e,
                            method: "GET"
                        }).then(function(e) {
                            return r(e.data)
                        }),
                        builds: n({
                            url: "/wp-json/wp/v2/builds?per_page=100&lang=" + e,
                            method: "GET"
                        }).then(function(e) {
                            var n = [];
                            return angular.forEach(e.data, function(e) {
                                var a = {
                                    id: e.id,
                                    title: t(e.title.rendered),
                                    copy: e.content.rendered,
                                    excerpt: e.excerpt.rendered
                                };
                                e.hasOwnProperty("warbands") && (a.warbands = e.warbands), e.acf && e.acf.cards && (a.cards = e.acf.cards), e.acf && e.acf.tactics && (a.tactics = e.acf.tactics), n.push(a)
                            }), n
                        })
                    }).then(function(e) {
                        return e
                    })
                },
                getCards: function(e, a) {
                    return n({
                        url: "/wp-json/wp/v2/cards?per_page=" + e + "&lang=" + a,
                        method: "GET"
                    }).then(function(e) {
                        var n = [];
                        return angular.forEach(e.data, function(e) {
                            var a;
                            e && e.acf && e.acf.card_number && (a = e.acf.card_number);
                            var a = "";
                            e.acf && e.acf.card_number && (a = e.acf.card_number);
                            var r = {
                                id: e.id,
                                name: t(e.title.rendered),
                                number: a,
                                type: null,
                                set: null,
                                faction: null,
                                isVisible: !0
                            };
                            e && e.acf && e.card_types.length && (r.type = e.card_types[0]), e && e.acf && e.warbands.length && (r.faction = e.warbands[0]), e && e.acf && e.sets.length && (r.set = e.sets[0]), e && e.acf && e.acf.card_image && (r.image = e.acf.card_image), n[e.id] = r
                        }), n
                    })
                }
            }
        }]).controller("DeckGuideController", ["$scope", "ApiClient", function(e, a) {
            function r() {
                function a() {
                    for (var t = 0; t < e.builds.length; t++) {
                        var n = e.builds[t];
                        if (u(n.warbands)) return o.build = n, n
                    }
                }

                function r(t) {
                    e.closeDeckMenu();
                    for (var n = 0; n < e.cards.length; n++) {
                        e.cards[n] !== t && (e.cards[n].showImage = !1)
                    }
                }

                function s(t, n) {
                    t.stopPropagation();
                    for (var a = n, r = 0; r < e.cards.length; r++) e.cards[r] && (e.cards[r].id !== a.ID ? e.cards[r].showImage = !1 : e.cards[r].showImage ? e.cards[r].showImage = !1 : e.cards[r].showImage = !0)
                }

                function i(e) {
                    var t = e.id;
                    o.buildId = t, o.build = e, f()
                }

                function c(e) {
                    o.currentWarband = e, i(a())
                }

                function l(t) {
                    e.currentFaction = t.id, e.currentDeck = t, e.deckMenuOpen = !1, e.deckMenu.isOpen = !1, c(t.id)
                }

                function d(e, t) {
                    return t.indexOf(parseInt(e)) > -1
                }

                function u(e) {
                    return e.indexOf(o.currentWarband) > -1
                }

                function p() {
                    return o.currentWarband
                }

                function f() {
                    var n = o.build.cards;
                    if (e.requiredDecks = [], n)
                        for (var a = 0; a < n.length; a++) {
                            var r = n[a],
                                s = e.locations[e.cards[r.ID].set]; - 1 === e.requiredDecks.indexOf(s) && (s.name = t(s.name), e.requiredDecks.push(s))
                        }
                }
                e.cardGroupings = [{
                    typeIds: e.objectiveCards,
                    isPower: !1,
                    copy: "OBJECTIVE CARDS"
                }, {
                    typeIds: e.powerCards,
                    isPower: !0,
                    copy: "POWER CARDS"
                }], e.setWarband = c, e.setWarBandFaction = l, e.isInCurrentWarband = u, e.isInGroup = d, e.setCurrentBuildId = i, e.getWarbandId = p, e.toggleCardImage = s, e.closeCardImages = r, c(n), e.loaded = !0
            }
            var s = [],
                i = [],
                o = {
                    buildId: null,
                    currentWarband: null
                };
            e.objectiveCards = s, e.powerCards = i, e.menuOpen = !1, e.deckMenuOpen = !1, e.deckMenu = {
                isOpen: !1
            }, e.cards = [], e.factionsTemp = {}, e.factions = {}, e.locations = [], e.currentItem = o, e.requiredDecks = [], e.loaded = !1, e.currentDeck = [], e.currentFaction = void 0, a.getData($("#selectedLang").val()).then(function(t) {
                function s() {
                    e.deckMenu.isOpen = !0
                }

                function i() {
                    e.deckMenu.isOpen = !1
                }

                function o(t) {
                    t.stopPropagation(), e.deckMenu.isOpen ? i() : s()
                }
                t = e.data = t, e.factionsTemp = t.factions, e.builds = t.builds, e.locations = t.locations, e.types = t.types;
                for (var c = [], l = 0; l < e.builds.length; l++) c = c.concat(e.builds[l].warbands);
                for (var d = 0; d < c.length; d++) e.factions[c[d]] = e.factionsTemp[c[d]], e.currentFaction = Object.keys(e.factions)[0], e.currentItem.currentWarband = e.currentFaction.id;
                !n && c.length > 0 && (n = e.factions[Object.keys(e.factions)[0]].id, e.currentDeck = e.factions[Object.keys(e.factions)[0]]);
                for (var u in t.types) t.types[u].hasOwnProperty("type") && ("power" === t.types[u].type && e.powerCards.push(t.types[u].id), "objective" === t.types[u].type && e.objectiveCards.push(t.types[u].id));
                a.getCards(1e3, $("#selectedLang").val()).then(function(t) {
                    e.cards = t, r()
                }), e.toggleDeckMenu = o, e.openDeckMenu = s, e.closeDeckMenu = i, angular.element(window.document).bind("click", i)
            })
        }])
    }
}(window.shade = window.shade || {}, angular),
function(e) {
    "use strict";
    e.eventsListing = {
        init: function() {
            function e() {
                var e = {
                    action: "events_loadposts",
                    query: soak_events_params.posts,
                    page: soak_events_params.current_page
                };
                return "null" != s.val() && (e.country = s.val()), e
            }

            function t() {
                $.ajax({
                    url: soak_events_params.ajaxurl,
                    data: e(),
                    type: "POST",
                    beforeSend: function() {
                        r.text("Loading...")
                    },
                    success: function(e) {
                        e ? (a.append(e), r.text("Show more Clash Events"), ++soak_events_params.current_page == soak_events_params.max_page && r.fadeOut()) : e || 0 != soak_events_params.current_page ? r.fadeOut() : i.fadeIn()
                    }
                })
            }

            function n() {
                soak_events_params.current_page = 0, i.hide(), a.html(""), r.fadeIn()
            }
            var a = jQuery("#ajax-events-container"),
                r = jQuery(".js-events-listing-loader"),
                s = jQuery("#event-listings_select-country"),
                i = jQuery(".js-events-error-handler");
            r.on("click", function() {
                t()
            }), s.on("change", function() {
                n(), t()
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e) {
    "use strict";
    e.featuredProduct = {
        init: function() {
            if ($(".js-update-featured-image").length > 0) {
                var e = $($(".featured-product__thumbnails").find(".js-update-featured-image img")[0]).attr("data-large");
                $(".js-featured-target").attr("src", e)
            }
            $(".js-update-featured-image").on("click", function() {
                var e = $(this).find("img");
                e.attr("data-large").length > 0 && $(".js-featured-target").attr("src", e.attr("data-large"))
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e) {
    "use strict";
    e.icons = {
        init: function() {
            var e = new XMLHttpRequest;
            e.open("GET", templateUrl + "/library/images/hex-colour.svg", !0), e.send(), e.onload = function() {
                var t = document.createElement("div");
                t.classList.add("hide-svg"), t.innerHTML = e.responseText, document.body.insertBefore(t, document.body.childNodes[0])
            }
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e) {
    "use strict";
    e.newsletter = {
        init: function() {
            function e(e) {
                jQuery.ajax({
                    url: e.attr("action"),
                    type: "get",
                    data: e.serialize(),
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    success: function(t) {
                        "success" != t.result ? e.addClass("errors") : (e.removeClass("errors"), e.addClass("successful"), GW.sendTrackingEvent("Form Submission", "Registration", "Email Newsletter"))
                    }
                })
            }
            var t = jQuery(".footer__newsletter");
            t.length > 0 && jQuery('input[type="submit"]', t).bind("click", function(n) {
                n && n.preventDefault(), e(t)
            });
            var n = jQuery("footer form");
            n.length > 0 && jQuery('input[type="submit"]', n).bind("click", function(t) {
                t && t.preventDefault(), e(n)
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e) {
    "use strict";
    var t = [{
        featureType: "administrative",
        elementType: "all",
        stylers: [{
            saturation: "-100"
        }]
    }, {
        featureType: "administrative.province",
        elementType: "all",
        stylers: [{
            visibility: "off"
        }]
    }, {
        featureType: "landscape",
        elementType: "all",
        stylers: [{
            saturation: -100
        }, {
            lightness: 65
        }, {
            visibility: "on"
        }]
    }, {
        featureType: "poi",
        elementType: "all",
        stylers: [{
            saturation: -100
        }, {
            lightness: "50"
        }, {
            visibility: "simplified"
        }]
    }, {
        featureType: "road",
        elementType: "all",
        stylers: [{
            saturation: "-100"
        }]
    }, {
        featureType: "road.highway",
        elementType: "all",
        stylers: [{
            visibility: "simplified"
        }]
    }, {
        featureType: "road.arterial",
        elementType: "all",
        stylers: [{
            lightness: "30"
        }]
    }, {
        featureType: "road.local",
        elementType: "all",
        stylers: [{
            lightness: "40"
        }]
    }, {
        featureType: "transit",
        elementType: "all",
        stylers: [{
            saturation: -100
        }, {
            visibility: "simplified"
        }]
    }, {
        featureType: "transit.station",
        elementType: "all",
        stylers: [{
            visibility: "off"
        }]
    }, {
        featureType: "transit.station.rail",
        elementType: "all",
        stylers: [{
            visibility: "off"
        }]
    }, {
        featureType: "water",
        elementType: "geometry",
        stylers: [{
            hue: "#ffff00"
        }, {
            lightness: -25
        }, {
            saturation: -97
        }]
    }, {
        featureType: "water",
        elementType: "labels",
        stylers: [{
            lightness: -25
        }, {
            saturation: -100
        }]
    }];
    e.storeMap = {
        init: function() {
            function e(e) {
                return k + "map-pin.png"
            }

            function n(t, n, a) {
                var s = ((.000621371192 * n.distance).toFixed(1), n.name),
                    i = n.address1,
                    o = n.address2,
                    c = n.address3,
                    l = n.state,
                    d = n.city,
                    u = n.postalCode,
                    p = n.telephone,
                    g = n.latitude,
                    w = n.longitude,
                    v = e(n),
                    b = n.seoUrl,
                    y = new google.maps.LatLng(g, w),
                    _ = '<h1 class="store-map__info-heading">' + s + "</h1>",
                    k = i ? "<p>" + i + ",</p>" : "",
                    j = o ? "<p>" + o + ",</p>" : "",
                    x = c ? "<p>" + c + ",</p>" : "",
                    C = l ? "<p>" + l + ",</p>" : "",
                    T = d ? "<p>" + d + ",</p>" : "",
                    $ = u ? "<p>" + u + "</p>" : "",
                    S = p ? "<p>" + p + "</p>" : "",
                    I = b ? '<div class="store-map__result-link">' + window.stringViewStories + "</div>" : "",
                    P = '<div class="store-map__info-window">' + _ + '<div class="store-map__info-body">' + k + j + x + C + T + $ + S + I + "</div></div>",
                    O = new google.maps.InfoWindow({
                        content: P
                    }),
                    D = (new google.maps.MarkerImage(v, new google.maps.Size(30, 30), new google.maps.Point(0, 0), new google.maps.Point(0, 0), new google.maps.Size(30, 30)), new google.maps.Marker({
                        position: y,
                        label: {
                            text: a.toString(),
                            color: "white"
                        },
                        map: t,
                        title: s,
                        icon: v,
                        animation: google.maps.Animation.DROP
                    }));
                f.push(D), D.addListener("click", function() {
                    r(), O.open(t, D), h = O
                }), m.extend(y)
            }

            function a(e, t, n) {
                var a = t.name,
                    r = t.address1,
                    s = t.address2,
                    i = t.address3,
                    o = t.state,
                    c = t.city,
                    l = t.postalCode,
                    d = t.telephone,
                    u = t.seoUrl,
                    p = '<h1 class="store-map__result-heading">' + a + "</h1>",
                    f = r ? "<p>" + r + ",</p>" : "",
                    g = s ? "<p>" + s + ",</p>" : "",
                    h = i ? "<p>" + i + ",</p>" : "",
                    w = o ? "<p>" + o + ",</p>" : "",
                    m = c ? "<p>" + c + ",</p>" : "",
                    v = l ? "<p>" + l + ",</p>" : "",
                    b = d ? "<p>" + d + ",</p>" : "",
                    y = u ? '<a href="' + u + '" class="store-map__result-link">' + window.stringViewStories + "</a>" : "",
                    _ = '<div class="store-map__result"><div class="store-map__result-inner"><div class="store-map__result-number">' + n + "</div>" + p + '<div class="store-map__result-body">' + f + g + h + w + m + v + b + y + "</div></div></div>";
                $(".js-store-results-wrapper").append(_)
            }

            function r() {
                h && h.close()
            }

            function s() {
                r();
                for (var e = 0; e < f.length; e++) f[e].setMap(null);
                f.length = 0, m = new google.maps.LatLngBounds
            }

            function i() {
                $(".js-store-results-wrapper").empty()
            }

            function o(e) {
                s(), i(), y.classList.add(_);
                for (var t = 0; t < e.length; t++) {
                    var r = e[t],
                        o = t;
                    o++, n(u, r, o), a(u, r, o)
                }
                u.fitBounds(m)
            }

            function c(e, t) {
                e.geocode({
                    address: t
                }, function(e, n) {
                    "OK" === n ? l(e[0].geometry.location) : alert(t + " not found")
                })
            }

            function l(e) {
                var t = e.lat(),
                    n = e.lng(),
                    a = "/api/store-search?lat=" + t + "&lng=" + n + "&limit=" + x + "&distance=" + j;
                console.log(e), $.get(a, function(e) {
                    g = e, e.length ? ($("div").remove(".store-map__error"), o(g)) : (s(), i())
                })
            }

            function d() {
                u = new google.maps.Map(v, {
                    styles: t
                }), w = !0
            }
            var u, p, f = [],
                g = [],
                h = null,
                w = !1,
                m = new google.maps.LatLngBounds,
                v = document.querySelector(".js-store-map-wrapper"),
                b = "store-map__wrapper--active",
                y = document.querySelector(".js-store-map-line"),
                _ = "vertical-separator__line--base-inner",
                k = "/wp-content/themes/gw-shadespire/library/images/",
                j = 500,
                x = 8;
            p = new google.maps.Geocoder,
                function() {
                    v.classList.add(b), w || d(), c(p, "NG7 2WS")
                }(), jQuery("#searchButton").on("click", function(e) {
                    e.preventDefault();
                    var t = jQuery("#store-map__location").val();
                    return v.classList.add(b), w || d(), c(p, t), !1
                })
        }
    }
}(window.GW = window.GW || {}, jQuery),
function(e) {
    "use strict";
    e.parallax = {
        init: function() {
            $(".parallax-banner").each(function() {
                var e = $(this).data("image-src");
                $(this).parallax({
                    imageSrc: e
                })
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e, t) {
    "use strict";
    e.fighters = {
        init: function() {}
    }
}(window.shade = window.shade || {}, jQuery),
function(e, t) {
    "use strict";
    e.promoSlider = {
        init: function() {
            t(".js-promo-slider").slick({
                dots: !0,
                arrows: !1,
                dotsClass: "promo-block__dots slick-dots",
                asNavFor: ".js-promo-btn-slider"
            }), t(".js-promo-btn-slider").slick({
                centerMode: !0,
                centerPadding: "0",
                dots: !1,
                arrows: !1,
                asNavFor: ".js-promo-slider"
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e, t) {
    "use strict";
    e.warbands = {
        init: function() {
            function e() {
                var e = t(".slick-center").data("colour");
                t(".warbands__controls-hex").removeClass(function(e, t) {
                    return (t.match(/(^|\s)warbands__controls-hex-\S+/g) || []).join(" ")
                }), t(".warbands__controls-hex").addClass("warbands__controls-hex-" + e)
            }
            t(".js-warband-slider").slick({
                infinite: !0,
                centerMode: !0,
                focusOnSelect: !0,
                centerPadding: "0",
                dragable: !1,
                slidesToShow: 9,
                slidesToScroll: 1,
                mobileFirst: !0,
                variableWidth: !0,
                nextArrow: '<button type="button" class="warbands__controls-next warbands__controls-btn slick-next"></button>',
                prevArrow: '<button type="button" class="warbands__controls-prev warbands__controls-btn slick-prev"></button>',
                responsive: [{
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 3
                    }
                }]
            }), e(), t(".js-warband-slider").on("afterChange", function() {
                e()
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e) {
    "use strict";
    e.stickyNav = {
        init: function() {
            $(".hero-banner__top").sticky({
                zIndex: 11
            }), $(".hero-block__top").sticky({
                zIndex: 11
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e) {
    "use strict";
    e.tabCard = {
        init: function() {
            function e(e) {
                function n() {
                    for (var e = 0; e < r.length; e++) {
                        var n = r[e];
                        n === this ? (this.classList.add(t), i[e].classList.add(t)) : (i[e].classList.remove(t), n.classList.remove(t))
                    }
                }

                function a() {
                    var e = $(this).val();
                    $(".content-tabs__tabs-content").removeClass(t), $("#" + e).addClass(t)
                }
                for (var r = e.querySelectorAll(".content-tabs__tab-tabs-desktop .content-tabs__tabs-header"), s = e.querySelectorAll(".content-tabs__tab-tabs-mobile"), i = (e.querySelectorAll(".content-tabs__tab-tabs-mobile .content-tabs__tabs-header"), e.querySelectorAll(".content-tabs__tabs-content")), o = 0; o < r.length; o++) r[o].addEventListener("click", n);
                for (var c = 0; c < s.length; c++) s[c].addEventListener("change", a)
            }
            for (var t = "active", n = document.querySelectorAll(".content-tabs__wrapper"), a = 0; a < n.length; a++) {
                new e(n[a])
            }
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e, t) {
    "use strict";
    e.tomb = {
        init: function() {
            t("body").mousemove(function(e) {
                t(".mask").css("background-position", e.pageX - 1750 + "px " + (e.pageY - 1250) + "px")
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e) {
    "use strict";
    e.warAjax = {
        init: function() {
            function e() {
                return {
                    asNavFor: ".js-fighter-info-slider",
                    centerMode: !0,
                    centerPadding: "0",
                    dragable: !1,
                    focusOnSelect: !0,
                    infinite: !0,
                    mobileFirst: !0,
                    initialSlide: 0,
                    slidesToScroll: 1,
                    slidesToShow: 3,
                    variableWidth: !0,
                    nextArrow: '<button type="button" class="fighter-block__controls-next slick-next"></button>',
                    prevArrow: '<button type="button" class="fighter-block__controls-prev slick-prev"></button>',
                    responsive: [{
                        breakpoint: 768,
                        settings: {
                            slidesToShow: 3
                        }
                    }]
                }
            }

            function t() {
                return {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                    swipe: !1,
                    dragable: !1,
                    focusOnSelect: !1,
                    arrows: !1,
                    fade: !0,
                    asNavFor: ".js-fighter-slider"
                }
            }

            function n() {
                var n = $(".slick-center").data("link");
                return $("#js-container").html('<div class="warband-loader">' + stringLoading + '<ul class="warband-drops"> <li></li> <li></li> <li></li> <li></li> <li></li> </ul> <div'), $("#js-container").load(n + " .content-wrapper"), $(document).ajaxComplete(function() {
                    $(".js-fighter-slider").slick(e()), $(".js-fighter-info-slider").slick(t())
                }), !1
            }
            var a = ".js-fighter-block-rotate-card";
            $(document).on("click", ".js-fighter-block-rotate-cards", function() {
                $(a).toggleClass("fighter-block-info__item-card--active")
            }), $(document).on("click", a, function() {
                return $.fancybox.open({
                    src: $(this).data("element"),
                    type: "inline"
                }), !1
            }), $(".js-fighter-slider").slick(e()), $(".js-fighter-info-slider").slick(t()), $.ajaxSetup({
                cache: !1
            }), n(), $(".js-warband-slider").on("afterChange", function() {
                n()
            }), $(".text-block").each(function() {
                $(this).next("div").hasClass("warbands") && $(this).addClass("bottom-padding")
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e, t) {
    "use strict";
    e.cardDeck = {
        init: function() {
            t(".js-card-deck-filter-toggle").on("click", function(e) {
                e.preventDefault(), t(this).toggleClass("js-card-deck-filter-toggle--active"), t(".js-card-deck-filter").slideToggle()
            })
        }
    }
}(window.shade = window.shade || {}, jQuery),
function(e, t, n) {
    "use strict";
    t(function() {
        e.warbands.init(), e.fighters.init(), e.tomb.init(), e.icons.init(), e.warAjax.init(), e.promoSlider.init(), e.tabCard.init(), e.burger.init(), e.newsletter.init(), e.parallax.init(), e.stickyNav.init(), e.cardDeck.init(), e.eventsListing.init(), e.featuredProduct.init()
    })
}(window.shade = window.shade || {}, jQuery);