/*global define*/
define([
    "collections/template",
    "common-objects/views/base",
    "text!templates/document/document_template_select.html"
], function (TemplateList, BaseView, template) {
    var DocumentTemplateListView = BaseView.extend({

        template: template,
        collection: function () {
            return TemplateList.getInstance();
        },
        initialize: function (options) {
            BaseView.prototype.initialize.apply(this, arguments);
            this.attributesView = options.attributesView;
            this.events["change"] = "changed";
        },
        collectionReset: function () {
            this.render();
        },
        collectionToJSON: function () {
            var data = BaseView.prototype.collectionToJSON.call(this);
            // Insert the empty option
            data.unshift({
                id: ""
            });
            return data;
        },
        selected: function () {
            var id = $("#select-" + this.cid).val();
            return this.collection.get(id);
        },
        changed: function () {
            // Reset reference field
            this.parentView.$el.find("input.reference:first")
                .unmask()
                .val("");
            // Insert Template attributes if any
            var collection = [];
            var template = this.selected();
            if (template) {
                var attributes = template.get("attributeTemplates");
                for (var i = attributes.length - 1; i >= 0; i--) {
                    collection.unshift({
                        type: attributes[i].attributeType,
                        name: attributes[i].name,
                        mandatory: attributes[i].mandatory,
                        value: ""
                    });
                }
                if (template.get("mask")) {
                    // Set field mask
                    this.showMask(template);
                }
                if (template.get("idGenerated")) {
                    this.generateId(template);
                }

                this.attributesView.setAttributesLocked(template.isAttributesLocked());
            }


            this.attributesView.collection.reset(collection);
        },
        showMask: function (template) {
            var elId = this.parentView.$el.find("input.reference:first");
            var mask = template.get("mask");//.replace(/#/g, "9");
            elId.mask(mask);
        },
        generateId: function (template) {
            var elId = this.parentView.$el.find("input.reference:first");
            // Get the next id from the webservice if any
            $.getJSON(template.url() + "/generate_id", function (data) {
                if (data) {
                    elId.val(data.id);
                }
            }, "html"); // TODO: fixe the webservice return type (actualy: json)
        }
    });
    return DocumentTemplateListView;
});