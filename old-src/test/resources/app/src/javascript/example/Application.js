goog.provide('example.Application');
goog.require('goog.ui.Component');
goog.require("goog.dom.classes");
goog.require("template.example");

/**
 * @constructor
 * @extends {goog.ui.Component}
 */
example.Application = function() {
  goog.base(this);
};
goog.inherits(example.Application, goog.ui.Component);


/**
 * @override
 */
example.Application.prototype.enterDocument = function() {
  goog.base(this, 'enterDocument');
  var element = this.getElementStrict();
  goog.dom.classes.add(element, goog.getCssName("example-Application"));
};

/** @override */
example.Application.prototype.createDom = function() {
  var html = template.example.Foo({});
  var element = goog.dom.htmlToDocumentFragment(html);
  this.setElementInternal(element);
};
