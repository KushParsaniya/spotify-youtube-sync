-- Find the name of the unique constraint on the username column


-- Replace 'username_unique' with the actual constraint name you found
ALTER TABLE users DROP CONSTRAINT users_username_key;

-- Add a unique constraint to the spotify_user_id column
ALTER TABLE users ADD CONSTRAINT spotify_user_id_unique UNIQUE(spotify_user_id);
ALTER TABLE users ADD CONSTRAINT youtube_user_id_unique UNIQUE(youtube_user_id);