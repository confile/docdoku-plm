/*global _,$,define,App*/
define(['backbone'], function (Backbone) {
    'use strict';
    var ConfigurationItem = Backbone.Model.extend({
        idAttribute: '_id',

        urlRoot: function () {
            return App.config.contextPath + '/api/workspaces/' + App.config.workspaceId + '/products';
        },

        initialize: function () {
            _.bindAll(this);
        },

        parse: function (response) {
            response._id = response.id;
            return response;
        },

        getId: function () {
            return this.get('id');
        },

        getDesignItemNumber: function () {
            return this.get('designItemNumber');
        },

        getIndexUrl: function () {
            return App.config.contextPath + '/product-structure/#' + App.config.workspaceId + '/' + encodeURIComponent(this.getId());
        },

        getFrameUrl: function () {
            return  App.config.contextPath + '/visualization/#' + App.config.workspaceId + '/' + this.getId() + '/0/10/1000/null/'+App.config.configSpec;
        },

        createBaseline: function (baselineArgs, callbacks) {
            $.ajax({
                type: 'POST',
                url: this.urlRoot() + '/' + this.getId() + '/baselines',
                data: JSON.stringify(baselineArgs),
                contentType: 'application/json; charset=utf-8',
                success: callbacks.success,
                error: callbacks.error
            });
        },

        deleteBaselines: function (baselines, callbacks) {
            var _this = this;
            var toDelete = _.size(baselines);
            _.each(baselines, function (baseline) {
                _this.deleteBaseline(baseline.getId(), {
                    success: function (data) {
                        toDelete--;
                        if(toDelete===0 && callbacks && _.isFunction(callbacks.success)){
                            callbacks.success(data);
                        }
                    },
                    error: function(err){
                        if(callbacks && _.isFunction(callbacks.error)){
                            callbacks.error(err);
                        }
                    }
                });
            });
        },

        deleteBaseline: function (baselineId, callbacks) {
            $.ajax({
                type: 'DELETE',
                async: false,
                url: this.urlRoot() + '/' + this.getId() + '/baselines/' + baselineId,
                contentType: 'application/json; charset=utf-8',
                success: function(data) {
                    if (callbacks && _.isFunction(callbacks.success)) {
                        callbacks.success(data);
                    }
                },
                error: function(err) {
                    if (callbacks && _.isFunction(callbacks.error)) {
                        callbacks.error(err);
                    }
                }
            });
        }

    });

    return ConfigurationItem;

});
