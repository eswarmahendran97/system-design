
Video streaming platform

Upload phase

Frontend user -> adds video -> videos is seperated as chunk -> each chunk is sent to upload service

upload service -> receives the chunk -> transform the chunk and add to s3 (origin storage)
once all chunks are recevied -> notifies streaming service

streaming service -> takes data from s3 -> transcode and make different quality -> make it to chunks -> frames manifest -> upload chunks to s3 and upload manifest to db


Playback phase

Frontend -> clicks on a video -> reveives Title, desciption, comments, metadata and manifest, manifest has signed url of chunk (to take from cdn)
cdn -> if present in cache -> gives video chunk directly
cdn -> if not present in cache -> takes from s3, caches it and send to user


live stream phase

Frontend Captures Video

API used: getUserMedia() in browser.
What it does:
Asks user for camera/microphone access.
Returns a MediaStream object containing:
Video track → sequence of frames (images captured at ~30 fps).
Audio track → audio samples.

Asynchronous stream:
Browser continuously receives frames from the camera as they are captured.

Frontend Encodes & Packages:
Frames are packaged into WebRTC media packets.

then asusal streaming service handles it.



You don’t get a single video file; you get a live stream of frames.

Why chunk?
User dont have to wait for whole video to get delivered.
In between he can change quality