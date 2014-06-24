goog.provide('Basic');
goog.require('goog.testing.jsunit');
goog.require('enable_continuation_testing');

/**
 * Test
 */
Basic.testBoolean = function() {
  assertTrue(true);
  assertFalse(false);
};
goog.exportSymbol("testBoolean", Basic.testBoolean);


/**
 * Test
 */
Basic.testGoogBase = function() {

  /**
   * @constructor
   */
  var A = function() {};

  /**
   * @constructor
   * @extends {A}
   */
  var B = function() {
    goog.base(this);
  };
  goog.inherits(B, A);

  var b = new B();
  assertTrue(b instanceof A);

};
goog.exportSymbol("testGoogBase", Basic.testGoogBase);