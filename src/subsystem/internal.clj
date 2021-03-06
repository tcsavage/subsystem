(ns subsystem.internal
  "Subsystem internals."
  {:author "Tom Savage"}
  (:require [clojure.set :as set]
            [com.stuartsierra.component :as component]))

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
    [__start __stop __system]
  component/Lifecycle
  (start [this] (__start this))
  (stop [this] (__stop this)))

(def component-dependencies
  "Returns keys depended on by component."
  (comp set vals :com.stuartsierra.component/dependencies meta))

(defn external-dependencies
  "Returns set of all dependency keys external to the system map."
  [system]
  (let [system-deps (set (mapcat component-dependencies (vals system)))
        system-keys (set (keys system))]
    (set/difference system-deps system-keys)))
