(ns subsystem.core-test
  (:require [clojure.test :refer :all]
            [subsystem.core :refer :all]
            [com.stuartsierra.component :as component]))

(def component-a
  (component/using
   {:name "component-a"}
   [:dependency-a]))

(def component-b
  (component/using
   {:name "component-b"}
   [:dependency-a :dependency-b]))

(def component-c
  {:name "component-c"})

(def inner-system
  {:component-a component-a
   :component-b component-b
   :component-c component-c})

(def inner-system-no-deps
  {:component-c component-c})

(defrecord TrackerComponent
    [running]
  component/Lifecycle
  (start [this]
    (assoc this :running true))
  (stop [this]
    (assoc this :running false)))

(defn tracking-component
  [init]
  (->TrackerComponent
   (case init
     :running true
     :stopped false)))

(defn running?
  [tracker]
  (:running tracker))

(deftest test-subsystem-inherits-dependencies
  (testing "When system components have external dependencies they should be
           inherited by the subsystem component"
    (let [subsystem-component (subsystem inner-system)
          inherited-deps (component-dependencies subsystem-component)
          external-deps (external-dependencies inner-system)]
      (is (= (set inherited-deps) external-deps))))

  (testing "When the system components have no external dependencies the
           subsystem component has no dependencies"
    (let [subsystem-component (subsystem inner-system-no-deps)
          inherited-deps (component-dependencies subsystem-component)]
      (is (nil? inherited-deps)))))

(deftest test-subsystem-start
  (testing "When subsystem component starts, inner system should start and
           started system map should be assoced into :system"
    (let [system (component/system-map
                  :component (tracking-component :stopped))
          started (component/start (subsystem system))]
      (is (running? (:component (:system started)))))))

(deftest test-subsystem-stop
  (testing "When subsystem component stops, inner system should stop and
           stopped system map should be assoced into :system"
    (let [system (component/system-map
                  :component (tracking-component :running))
          started (component/start (subsystem system))
          stopped (component/stop started)]
      (is (not (running? (running? (:component (:system stopped)))))))))

(defn push-meta
  [value]
  (fn [system]
    (vary-meta
     system
     update :test-meta (partial cons value))))

(defn test-meta
  [subsystem]
  (-> subsystem
      :system
      meta
      :test-meta))

(defn assert-component-running
  [system]
  (is (running? (:component system)))
  system)

(defn assert-component-stopped
  [system]
  (is (not (running? (:component system))))
  system)

(deftest test-subsystem-start-actions
  (testing "When subsystem component starts, inner system should start and
           started system map should be assoced into :system"
    (let [pre-start (comp assert-component-stopped (push-meta :pre-start))
          post-start (comp assert-component-running (push-meta :post-start))
          system (component/system-map
                  :component (tracking-component :stopped))
          started (component/start (subsystem system
                                              :pre-start pre-start
                                              :post-start post-start))]
      (is (running? (:component (:system started))))
      (is (= '(:post-start :pre-start) (test-meta started))))))

(deftest test-subsystem-stop-actions
  (testing "When subsystem component starts, inner system should start and
           started system map should be assoced into :system"
    (let [pre-stop (comp assert-component-running (push-meta :pre-stop))
          post-stop (comp assert-component-stopped (push-meta :post-stop))
          system (component/system-map
                  :component (tracking-component :stopped))
          started (component/start (subsystem system
                                              :pre-stop pre-stop
                                              :post-stop post-stop))
          stopped (component/stop started)]
      (is (not (running? (:component (:system stopped)))))
      (is (= '(:post-stop :pre-stop) (test-meta stopped))))))
