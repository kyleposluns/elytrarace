package com.kyleposluns.elytrarace.tracking;

public interface CheckpointTrackerVisitor<R> {

  R visitCheckpointTrackerImpl(CheckpointTrackerImpl checkpointTracker);

}
