webix.protoUI({
    name: 'custom-protoui',
    defaults: {
        cols: [],
        width: 187,
        margin: 0,
        padding: 0,
        css: 'entity-dropdowns'
    },
    $init(config) {
        this.$ready.push(() => {
            this.addView(componentConfig(config));
        });
    }
}, webix.ui.layout);

const componentConfig = (config) => {    
    return {
            view: 'form', id: config.id + "-form", margin: 0, padding: 0,
            css: 'pop-range-form',
            width: 187,
            elements: [
                {
                    height: 20, width: 187, cols: [{ type: 'space', width: 14 }, {
                        view: "checkbox", paddingX: 3, css: "checkbox-label", height: 28, id: config.id + 'vs', name: 'vs', labelRight: '<strong>Very Small</strong> (0-500)', labelAlign: 'right', value: 0, labelWidth: 0
                    }]
                },
                { type: 'space', height: 10 },
                {
                    height: 20, width: 187, cols: [{ type: 'space', width: 14 }, {
                        view: "checkbox", paddingX: 3, css: "checkbox-label", height: 28, id: config.id + 's', name: 's', labelRight: '<strong>Small</strong> (501-3,300)', labelAlign: 'right', value: 0, labelWidth: 0
                    }]
                },
                { type: 'space', height: 10 },
                {
                    height: 20, width: 187, cols: [{ type: 'space', width: 14 }, {
                        view: "checkbox", paddingX: 3, css: "checkbox-label", height: 28, id: config.id + 'm', name: 'm', labelRight: '<strong>Medium</strong> (3,301-10,000)', labelAlign: 'right', labelWidth: 0
                    }]
                },
                { type: 'space', height: 10 },
                {
                    height: 20, width: 187, cols: [{ type: 'space', width: 14 }, {
                        view: "checkbox", paddingX: 3, css: "checkbox-label", height: 28, id: config.id + 'l', name: 'l', labelRight: '<strong>Large</strong> (10,001-100,000)', labelAlign: 'right', labelWidth: 0
                    }]
                },
                { type: 'space', height: 10 },
                {
                    height: 20, width: 187, cols: [{ type: 'space', width: 14 }, {
                        view: "checkbox", paddingX: 3, css: "checkbox-label", height: 22, id: config.id + 'vl', name: 'vl', labelRight: '<strong>Very Large </strong>(100,000 +)', labelAlign: 'right', labelWidth: 0
                    }]
                },
                { type: 'space', height: 10 },
                {
                    height: 20, width: 187, cols: [{ type: 'space', width: 14 }, {
                        view: "checkbox", paddingX: 3, width: 60, css: "checkbox-label", height: 22, id: config.id + 'anyBtnId', name: config.id + 'anyBtnId', labelRight: '<strong> Any </strong>', labelAlign: 'right', value: 0, labelWidth: 0
                    }, { type: 'space' },]
                },

                { type: 'space', height: 10 },
                {
                    view: 'layout', id: 'from_to_range', width: 187, height: 65, margin: 0, css: 'suggest_block', rows: [
                        //{ height: 18, css: '', cols: [{ type: 'space', width: 89 }, { view: "label", label: 'or', css: 'or-option' }, { type: 'space', width: 91 }] },
                        { type: 'space', height: 5 },
                        {
                            height: 37, width: 187, margin: 0, padding: 0, cols: [{ type: "space", width: 12 }, {
                                view: "text", width: 75, placeholder: "Min", name: 'populationFromRange', id: config.id + 'populationFromRange', disabled: true, on: {
                                    'onChange': function (newv, oldv) {
                                        $$("from_to_range").getParentView().validate();
                                    }
                                }
                            },
                            { view: "label", height: 20, label: '-', width: 10 }, {
                                view: "text", placeholder: "Max", width: 75, disabled: true, name: 'populationToRange', id: config.id + 'populationToRange', on: {
                                    'onChange': function (newv, oldv) {
                                        $$("from_to_range").getParentView().validate();
                                    }
                                }
                            }]
                        },
                        { type: 'space', height: 5 }
                    ]
                }
            ],
            rules: {
                "populationToRange": webix.rules.isNumber,
                "populationFromRange": webix.rules.isNumber,
            },
    };
}
