(ns subsystem.core
  {:author "Tom Savage"}
  (:require [subsystem.internal :as internal]
            [com.stuartsierra.component :as component]
            [clojure.algo.generic.functor :refer [fmap]]))

(defn subsystem
  "Turn a system map into a component. The resulting component inherits the
  dependencies of all constituent components. Components inside a subsystem can
  depend on each other and components in the main system, but main system
  components cannot depend on those inside the subsystem.

  Use `:[pre|post]-[start|stop]` keyword arguments to specify optional functions
  to be applied to the system map immediately before/after starting/stopping.

  ```
  (subsystem
    (component/system-map
      :comp1 (component/using comp1 [:external-comp])
      :comp2 (component/using comp2 [:comp1 :external]))
    :post-start some-operation)
  ```"
  [system & {:keys [pre-start post-start pre-stop post-stop]
             :or {pre-start identity post-start identity
                  pre-stop identity post-stop identity}}]
  (let [deps (internal/external-dependencies system)
        start (fn [this] (let [started (as-> this $
                                         (select-keys $ deps)
                                         (fmap internal/->ComponentBox $)
                                         (merge system $)
                                         (pre-start $)
                                         (component/start-system $)
                                         (post-start $))]
                           (assoc this :__system started)))
        stop (fn [this] (let [system (:__system this)
                              stopped (as-> system $
                                        (select-keys $ deps)
                                        (fmap internal/->ComponentBox $)
                                        (merge system $)
                                        (pre-stop $)
                                        (component/stop-system $)
                                        (post-stop $))]
                          (assoc this :__system stopped)))
        component (internal/map->Subsystem {:__start start :__stop stop})]
    (if (seq deps)
      (component/using component (vec deps))
      component)))
