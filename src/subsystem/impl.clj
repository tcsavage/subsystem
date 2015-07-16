(ns subsystem.impl
  "Subsystem internals."
  {:author "Tom Savage"}
  (:require [com.stuartsierra.component :as component]))

;;; A ComponentBox is a simple wrapper for components which are already running
;;; at start-time (like dependencies). When a ComponentBox starts it just
;;; returns the already-running component inside it. A ComponentBox should never
;;; be stopped.
(defrecord ComponentBox
    [component]
  component/Lifecycle
  (start [this]
    component)
  (stop [this]
    (throw (ex-info "A component box should never reach the point of being stopped."
                    {:component component}))))

(defrecord Subsystem
    [__start __stop]
  component/Lifecycle
  (start [this] (__start this))
  (stop [this] (__stop this)))