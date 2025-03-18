"use client"; // Error boundaries must be Client Components

import { useEffect } from "react";
import { Alert, Box, Container, Paper, Text } from "@mantine/core";
import { IconInfoCircle } from "@tabler/icons-react";

export default function Error({
  error,
}: {
  error: Error & { digest?: string };
}) {
  useEffect(() => {
    // Log the error to an error reporting service
    console.error(">>>> error: " + error);
  }, [error]);

  const icon = <IconInfoCircle />;

  return (
    <Alert variant="red" title="Sorry, something's not right" icon={icon}>
      <Text size="md" c="red">
        Try again later!
      </Text>
    </Alert>
  );
}
