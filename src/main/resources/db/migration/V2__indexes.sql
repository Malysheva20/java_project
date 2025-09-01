-- V2__indexes.sql
CREATE INDEX IF NOT EXISTS idx_tasks_user_active ON tasks(user_id) WHERE deleted = FALSE;
CREATE INDEX IF NOT EXISTS idx_tasks_user_pending ON tasks(user_id) WHERE completed = FALSE AND deleted = FALSE;
CREATE INDEX IF NOT EXISTS idx_notifications_user_pending ON notifications(user_id) WHERE read_flag = FALSE;
