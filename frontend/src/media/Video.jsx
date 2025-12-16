import { useEffect, useRef, useState } from "react";

const Video = ({
  path = null,
  className = "",
  fallback = null,
  controls = true,
  autoPlay = false,
  loop = false,
  muted = false,
  ...attributes
}) => {
  const [src, setSrc] = useState();
  const [loading, setLoading] = useState(false);

  const abortRef = useRef();

  useEffect(() => {
    if (path === null) return;
    let objectUrl;

    const getMedia = async () => {
      try {
        if (abortRef.current) abortRef.current.abort();
        abortRef.current = new AbortController();
        setLoading(true);

        const res = await api.get(`http://localhost:9090/api/files/${path}`, {
          responseType: "blob",
          signal: abortRef.current.signal
        });

        objectUrl = URL.createObjectURL(res.data);
        setSrc(objectUrl);
      } catch (err) {
        console.log("video fetch error:", err);
      } finally {
        setLoading(false);
      }
    };

    getMedia();

    return () => {
      if (abortRef.current) {
        abortRef.current.abort();
      }
      if (objectUrl) {
        URL.revokeObjectURL(objectUrl);
      }
    };
  }, [path]);

  if (loading) {
    return <div>Loading.....</div>;
  }

  return (
    <video
      src={src}
      className={className}
      controls={controls}
      autoPlay={autoPlay}
      loop={loop}
      muted={muted}
      {...attributes}
    />
  );
};

export default Video;