import { useEffect, useState, useRef } from "react";
import api from "../api/api";

const Image = ({
  path = null,
  className = "",
  fallback = null,
  ...attributes
}) => {
  const [src, setSrc] = useState(null);
  const [loading, setLoading] = useState(false);
  const abortRef = useRef();
  useEffect(() => {
    if (path === null) return;
    let objectUrl = null;
    const getData = async () => {
      try {
        if (abortRef.current) abortRef.current.abort();
        abortRef.current = new AbortController();
        setLoading(true);
        const res = await api.get(`api/files/${path}`, {
          responseType: "blob",
          signal: abortRef.current.signal,
        });
        objectUrl = URL.createObjectURL(res.data);
        setSrc(objectUrl);
      } catch (err) {
        console.log("image fetch err: ", err);
      } finally {
        setLoading(false);
      }
    };

    getData();
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
    <img src={src || fallback} className={className} alt="" {...attributes} />
  );
};
export default Image;
