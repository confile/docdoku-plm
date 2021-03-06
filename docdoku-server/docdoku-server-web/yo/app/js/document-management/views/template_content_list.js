/*global define,bootbox,App*/
define([
    'collections/template',
    'views/content',
    'views/template_list',
    'views/template_new',
    'text!templates/template_content_list.html',
    'text!common-objects/templates/buttons/delete_button.html'
], function (TemplateList, ContentView, TemplateListView, TemplateNewView, template, deleteButton) {
	'use strict';
	var TemplateContentListView = ContentView.extend({

        template: template,

        partials: {
            deleteButton: deleteButton
        },

        collection: function () {
            return TemplateList.getInstance();
        },
        initialize: function () {
            ContentView.prototype.initialize.apply(this, arguments);
            this.events['click .actions .new-template'] = 'actionNew';
            this.events['click .actions .delete'] = 'actionDelete';
        },
        rendered: function () {
            this.listView = this.addSubView(new TemplateListView({
                el: '#list-' + this.cid,
                collection: this.collection
            }));
            this.listView.collection.fetch({reset: true});
            this.listView.on('selectionChange', this.selectionChanged);
            this.selectionChanged();
        },
        selectionChanged: function () {
            var showOrHide = this.listView.checkedViews().length > 0;
            var action = showOrHide ? 'show' : 'hide';
            this.$el.find('.actions .delete')[action]();
        },
        actionNew: function () {
            this.addSubView(
                new TemplateNewView({
                    collection: this.collection
                })
            ).show();
            this.listView.redraw();
            return false;
        },
        actionDelete: function () {
            var that = this;

            bootbox.confirm(App.config.i18n.DELETE_SELECTION_QUESTION, function(result){
                if(result){
                    that.listView.eachChecked(function (view) {
                        view.model.destroy({
                            dataType: 'text', // server doesn't send a json hash in the response body
                            success: function () {
                                that.listView.redraw();
                            }
                        });
                    });
                }
            });

            return false;
        }
    });
    return TemplateContentListView;
});
