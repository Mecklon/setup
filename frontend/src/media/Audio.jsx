const Audio = ({
  path = null,
  className = "",
  fallback = null,
  controls = true,
  autoPlay = false,
  loop = false,
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
        console.log("audio fetch err: ", err);
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
    return <div>Loading....</div>;
  }

  return (
    <audio
      src={src}
      className={className}
      controls={controls}
      autoPlay={autoPlay}
      loop={loop}
      {...attributes}
    />
  );
};

export default Audio;
