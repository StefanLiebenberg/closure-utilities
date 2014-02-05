goog.provide('company.greeting');

/**
 * @param {string} name
 * @returns {string}
 */
company.greeting.hello = function (name) {
  /** @desc The hello message.  */
  var MSG_HELLO = goog.getMsg("Hello {$name}", {
    'name': name
  });

  return MSG_HELLO;
};
goog.exportSymbol("company.greeting.hello", company.greeting.hello);


/**
 * @param {string} name
 * @returns {string}
 */
company.greeting.goodbye = function (name) {
  /**
   * @desc The bye message.
   */
  var MSG_BYE = goog.getMsg("Good Bye {$name}", {
    'name': name
  });
  return MSG_BYE;
};
goog.exportSymbol("company.greeting.goodbye", company.greeting.goodbye);

/**
 * @param {string} name
 * @returns {string}
 */
company.greeting.welcome = function (name) {
  /**
   * @desc the Welcome message.
   */
  var MSG_WELCOME = goog.getMsg("Welcome {$name}", {
    'name': name
  });
  return MSG_WELCOME;
};
goog.exportSymbol("company.greeting.welcome", company.greeting.welcome);