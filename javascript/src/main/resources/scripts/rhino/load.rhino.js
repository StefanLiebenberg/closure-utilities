if (document.createEvent) {
  var event = document.createEvent('Event');
  event.initEvent('DOMContentLoaded', false, false);
  document.dispatchEvent(event, false);
  event = document.createEvent('HTMLEvents');
  event.initEvent('load', false, false);
  document.dispatchEvent(event, false);
}
try {
  if (document === window.document) {
    event = document.createEvent('HTMLEvents');
    event.initEvent('load', false, false);
    window.dispatchEvent(event, false);
  }
} catch (e) {
  console.log('window load event failed %s', e);
}