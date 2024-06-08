ALTER TABLE users
    ADD COLUMN app_id int REFERENCES oauth2_apps (app_id);