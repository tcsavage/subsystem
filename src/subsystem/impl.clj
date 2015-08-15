(ns subsystem.impl
  "Subsystem internals."
  {:author "Tom Savage"}
  (:require [com.stuartsierra.component :as component]))

;;; A ComponentBox is a simple wrapper for components which are already running
;;; at start-time (like dependencies). When a ComponentBox starts or stops it
;;; just returns the already-running or stopped component inside it.
(defrecord ComponentBox
    [component]
  component/Lifecycle
  (start [this]
    component)
  (stop [this]
    component))

(defrecord Subsystem
    [__start __stop]
  component/Lifecycle
  (start [this] (__start this))
  (stop [this] (__stop this)))
