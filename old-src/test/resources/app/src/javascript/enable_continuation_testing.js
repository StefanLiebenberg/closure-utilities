goog.provide('enable_continuation_testing');
goog.require('goog.testing.ContinuationTestCase');
goog.require('goog.testing.jsunit');


/**
 * Enables continuation in the betgenius testing environment.
 */
enable_continuation_testing = function() {
  var realTimeout = window.setTimeout, fn = window.onload, testCase, tr;
  window.onload = function(e) {
    if (fn) {
      fn(e);
    }
    tr = goog.global['G_testRunner'];
    if (!tr.initialized) {
      var test = new goog.testing.ContinuationTestCase(document.title);
      test.autoDiscoverTests();
      tr.initialize(test);
    }
    window.onload = null;
  };
};

enable_continuation_testing();
