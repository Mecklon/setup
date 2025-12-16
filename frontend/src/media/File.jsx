
import { useEffect, useRef, useState } from "react";
import api from "../api/api";
import { FaDownload } from "react-icons/fa";
import pdf from "../assets/pdf.png";
import word from "../assets/word.png";
import excel from "../assets/excel.png";
import ppt from "../assets/ppt.png";

const File = ({
  path = null,
  fileName = "file",
  fileType = null,
  ...attribute
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
    return <div>Loading......</div>;
  }

  return (
    <div
      onClick={(e) => e.stopPropagation()}
      className="text-sm flex gap-2 text-text bg-bg-light p-2 rounded-sm items-center cursor-auto"
    >
      <div className=" rounded-full h-17 w-17 shrink-0 p-3">
        {fileType === "application/pdf" ? (
          <img src={pdf} className="h-full" />
        ) : fileType === "text/plain" ? (
          <img src={word} className="h-full" />
        ) : fileType ===
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ? (
          <img className="h-full" src={excel} />
        ) : (
          <img className="h-full" src={ppt} />
        )}
      </div>
      <div className="grow break-all">Download {fileName}</div>
      <a href={src} download={fileName} id={`link-${fileName}`}>
        <div
          htmlFor={`link-${fileName}`}
          className="hover:bg-[rgba(0,0,0,0.5)] duration-200 cursor-pointer p-4 rounded-full shrink-0"
        >
          <FaDownload className="text-2xl" />
        </div>
      </a>
    </div>
  );
};

export default File;
