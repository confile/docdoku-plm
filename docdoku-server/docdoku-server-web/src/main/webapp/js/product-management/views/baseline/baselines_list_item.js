define([
    "text!templates/baseline/baselines_list_item.html",
    "i18n!localization/nls/baseline-strings"
], function (
    template,
    i18n
    ) {
    var ProductListItemView = Backbone.View.extend({

        template: Mustache.compile(template),

        events:{
            "click input[type=checkbox]":"selectionChanged",
            "click td.reference":"openEditView"
        },

        tagName:"tr",

        initialize: function () {
            this._isChecked = false ;
        },

        render:function(){
            this.$el.html(this.template({model:this.model, i18n:i18n}));
            this.$checkbox = this.$("input[type=checkbox]");
            this.trigger("rendered",this);
            return this;
        },

        selectionChanged:function(){
            this._isChecked = this.$checkbox.prop("checked");
            this.trigger("selectionChanged",this);
        },

        isChecked:function(){
            return this._isChecked;
        },

        check:function(){
            this.$checkbox.prop("checked", true);
            this._isChecked = true;
        },

        unCheck:function(){
            this.$checkbox.prop("checked", false);
            this._isChecked = false;
        },

        openEditView:function(){
            var that = this ;
            require(["views/baseline/baseline_edit_view"],function(BaselineEditView){
                var view = new BaselineEditView({model:that.model});
                $("body").append(view.render().el);
            });
        }
    });

    return ProductListItemView;
});