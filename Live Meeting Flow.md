Live Meeting Flow

1. Meeting Setup

User opens Zoom client / browser.
Joins a meeting → signaling server authenticates and provides:
    Meeting ID
    Participant IDs
    SFU server info (IP/port)
Browser opens WebRTC connection (peer connection) to the SFU.


2. Capturing Media

User talks or shares video.
Browser calls getUserMedia() → captures:
    Video track (frames, ~30 fps)
    Audio track (samples, Opus or AAC)
Browser encodes the tracks in real-time (VP8/VP9/H.264 for video).


3. Sending Media to Backend (SFU)

Encoded frames/audio are sent via WebRTC over the established connection.
Each packet includes metadata:
    Sender ID
    Track ID
    Timestamp


4. SFU / Backend Handling

SFU receives media from User A.
SFU knows all active participants in the meeting.
Pushes media selectively to other participants:
User B, C, D receive only the streams they are subscribed to.


5. Receiving Participants

Browser receives WebRTC packets pushed by SFU.
Browser decodes video/audio in real-time.
Plays video/audio via <video> / <audio> elements.