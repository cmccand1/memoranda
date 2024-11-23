/**
 * EventsScheduler.java Created on 10.03.2003, 20:20:08 Alex Package: net.sf.memoranda
 *
 * @author Alex V. Alishevskikh, alex@openmechanics.net Copyright (c) 2003 Memoranda Team.
 * http://memoranda.sf.net
 */
package memoranda.events;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 *
 */
public class EventsScheduler {

  static List<EventTimer> _timers = new ArrayList<>();
  static List<EventNotificationListener> _listeners = new ArrayList<>();

  static Timer changeDateTimer = new Timer();

  static {
    addListener(new DefaultEventNotifier());
  }

  public static void init() {
    cancelAll();
    //changeDateTimer.cancel();
    List<Event> events = (ArrayList<Event>) EventsManager.getActiveEvents();
    _timers = new ArrayList<>();
    /*DEBUG*/
    System.out.println("----------");
    for (Event event : events) {
      Event ev = event;
      Date evTime = ev.getTime();
      /*DEBUG*/
      System.out.println((Calendar.getInstance()).getTime());
      //  if (evTime.after(new Date())) {
      if (evTime.after((Calendar.getInstance()).getTime())) {
        EventTimer t = new EventTimer(ev);
        t.schedule(new NotifyTask(t), ev.getTime());
        _timers.add(t);
        /*DEBUG*/
        System.out.println(ev.getTimeString());
      }
    }
    /*DEBUG*/
    System.out.println("----------");
    Date midnight = getMidnight();
    changeDateTimer.schedule(new TimerTask() {
      public void run() {
        init();
        this.cancel();
      }
    }, midnight);
    notifyChanged();
  }

  public static void cancelAll() {
    for (EventTimer timer : _timers) {
      EventTimer t = timer;
      t.cancel();
    }
  }

  public static Vector getScheduledEvents() {
    Vector v = new Vector();
    for (EventTimer timer : _timers) {
      v.add(timer.getEvent());
    }
    return v;
  }

  public static Event getFirstScheduledEvent() {
    if (!isEventScheduled()) {
      return null;
    }
    Event e1 = _timers.get(0).getEvent();
    for (int i = 1; i < _timers.size(); i++) {
      Event ev = _timers.get(i).getEvent();
      if (ev.getTime().before(e1.getTime())) {
        e1 = ev;
      }
    }
    return e1;
  }


  public static void addListener(EventNotificationListener enl) {
    _listeners.add(enl);
  }

  public static boolean isEventScheduled() {
    return !_timers.isEmpty();
  }

  private static void notifyListeners(Event ev) {
    for (EventNotificationListener listener : _listeners) {
      listener.eventIsOccured(ev);
    }
  }

  private static void notifyChanged() {
    for (EventNotificationListener listener : _listeners) {
      listener.eventsChanged();
    }
  }

  private static Date getMidnight() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    cal.add(Calendar.DAY_OF_MONTH, 1);
    return cal.getTime();
  }

  static class NotifyTask extends TimerTask {

    EventTimer _timer;

    public NotifyTask(EventTimer t) {
      super();
      _timer = t;
    }

    public void run() {
      _timer.cancel();
      _timers.remove(_timer);
      notifyListeners(_timer.getEvent());
      notifyChanged();
    }
  }

  static class EventTimer extends Timer {

    Event _event;

    public EventTimer(Event ev) {
      super();
      _event = ev;
    }

    public Event getEvent() {
      return _event;
    }
  }
}
