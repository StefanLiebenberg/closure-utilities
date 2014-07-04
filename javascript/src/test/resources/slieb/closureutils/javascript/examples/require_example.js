goog.provide('example.requireExample');
goog.require('example.requireA');
goog.require('example.requireB');
//goog.require('slieb.closureutils.javascript.exampleRequireC');
/* goog.require('slieb.closureutils.javascript.exampleRequireD'); */

// this is odd, but still valid.
if (goog.isDefAndNotNull(window)) {
  goog.require('example.requireE');
}

(function() {
  goog.require('example.requireF');
})();