import { Box, Group, Image, SimpleGrid, Stack, Text } from "@mantine/core";
import { Dropzone, DropzoneProps, IMAGE_MIME_TYPE } from "@mantine/dropzone";
import { IconPhoto, IconUpload, IconX } from "@tabler/icons-react";
import { useState } from "react";
import { FileError, FileRejection } from "react-dropzone";
import { ErrorBanner } from "../ErrorBanner/Error";
type ImageGalleryProps = Partial<DropzoneProps> & {
  numImages?: number | 0;
};

export default function ImageGallery(props: ImageGalleryProps) {
  const [imageUploadError, setImageUploadError] = useState<string | null>(null);
  const FILE_UPLOAD_MAX_SIZE = process.env.NEXT_PUBLIC_FILE_UPLOAD_MAX_SIZE;
  const BFF_PRESIGNED_URL =
    process.env.NEXT_PUBLIC_PRESIGNED_URL || "/api/s3/presigned";
  const S3_BUCKET_NAME =
    process.env.NEXT_PUBLIC_S3_BUCKET_NAME || "post-images";

  function handleDrop(files: File[]) {
    console.log("accepted files", files);
    files.map((file) => {
      generatePresignedUrl(file)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to get presigned URL");
          }
          return response.json();
        })
        .then((data) => {
          console.log("Presigned URL data:", data);
          return uploadFileToS3(file, data.presignedUrl);
        })
        .then((response) => {
          if (!response.ok) {
            throw new Error("Failed to upload file to S3");
          }
          console.log("File uploaded to S3 successfully");
        })
        .catch((error) => {
          console.error("Error generating presigned URL:", error);
        });
    });
  }

  function uploadFileToS3(file: File, presignedUrl: string) {
    console.log("Uploading file to S3 with presigned URL:", presignedUrl);
    return fetch(presignedUrl, {
      method: "PUT",
      body: file,
    });
  }

  function generatePresignedUrl(file: File) {
    console.log("Generating presigned URL for file:", file);
    const response = fetch(BFF_PRESIGNED_URL + `/${S3_BUCKET_NAME}`, {
      cache: "no-store",
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        name: file.name,
        size: file.size,
        type: file.type,
        lastModified: file.lastModified,
      }),
    });
    return response;
  }

  function handleReject(fileRejections: FileRejection[]) {
    console.log("rejected files", fileRejections);
    const errorMessages = fileRejections.map((fileRejection) => {
      const errorMsg =
        "file: " +
        fileRejection.file.name +
        " was rejected. Reason: " +
        fileRejection.errors.map((e: FileError) => e.message).join(", ");
      return errorMsg;
    });
    setImageUploadError(errorMessages.join(". "));
  }

  function resetImageUploadError() {
    setImageUploadError(null);
  }

  // function customValidation(file: File) {
  //   if (file.size > parseInt(FILE_UPLOAD_MAX_SIZE || "2097152")) {
  //     const error: FileError = {
  //       message: `File size should not exceed ${parseInt(FILE_UPLOAD_MAX_SIZE || "2097152") / 1024 / 1024}mb`,
  //       code: "file-too-large",
  //     };
  //     return error;
  //   } else {
  //     return null;
  //   }
  // }

  return (
    <>
      <Stack>
        <div style={{ resize: "horizontal", maxWidth: "100%" }}>
          <SimpleGrid
            type="container"
            cols={{ base: 1, "300px": 2, "500px": 5 }}
            spacing={{ base: 10, "300px": "xl" }}
          >
            {props.numImages &&
              props.numImages > 0 &&
              [...Array(props.numImages)].map((_, index) => (
                <Box
                  key={index}
                  p="sm"
                  bd="1px solid gray"
                  style={{
                    minHeight: "100px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  <Image
                    key={`img-${index}`}
                    radius="xs"
                    h={50}
                    w="auto"
                    fit="fill"
                    src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/images/bg-9.png"
                    alt="image-gallery"
                  />
                </Box>
              ))}
          </SimpleGrid>

          {imageUploadError && (
            <ErrorBanner
              message={imageUploadError}
              onClose={resetImageUploadError}
              onRetry={null}
            />
          )}
        </div>

        <Box my="xl" bd={"1px solid gray"} p="xl">
          <Dropzone
            onDrop={handleDrop}
            onReject={handleReject}
            // validator={customValidation}
            maxSize={parseInt(FILE_UPLOAD_MAX_SIZE || "2097152")}
            accept={IMAGE_MIME_TYPE}
            {...props}
          >
            <Group
              justify="center"
              gap="xl"
              mih={220}
              style={{ pointerEvents: "none" }}
            >
              <Dropzone.Accept>
                <IconUpload
                  size={52}
                  color="var(--mantine-color-blue-6)"
                  stroke={1.5}
                />
              </Dropzone.Accept>
              <Dropzone.Reject>
                <IconX
                  size={52}
                  color="var(--mantine-color-red-6)"
                  stroke={1.5}
                />
              </Dropzone.Reject>
              <Dropzone.Idle>
                <IconPhoto
                  size={52}
                  color="var(--mantine-color-dimmed)"
                  stroke={1.5}
                />
              </Dropzone.Idle>

              <div>
                <Text size="xl" inline>
                  Drag images here or click to select files
                </Text>
                <Text size="sm" c="dimmed" inline mt={7}>
                  Attach as many files as you like, each file should not exceed
                  2mb in size
                </Text>
              </div>
            </Group>
          </Dropzone>
        </Box>
      </Stack>
    </>
  );
}
