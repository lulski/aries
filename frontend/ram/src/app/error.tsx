"use client"; // Error boundaries must be Client Components

import { Alert, Text } from "@mantine/core";
import { IconInfoCircle } from "@tabler/icons-react";
import { useEffect } from "react";

export default function Error({
  error,
}: {
  error: Error & { digest?: string };
}) {
  useEffect(() => {
    console.error(">>>> error: " + error);
  }, [error]);

  const icon = <IconInfoCircle />;

  return (
    <Alert
      variant="red"
      title="Whoop-de-doo, something's screwed the pooch!"
      icon={icon}
    >
      <Text size="md" c="red">
        Sorry lah!
      </Text>
    </Alert>
  );
}
