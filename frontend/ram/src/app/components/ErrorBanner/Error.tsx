import { Alert, Button, Group, Text } from "@mantine/core";
import { IconAlertCircle } from "@tabler/icons-react";

export function ErrorBanner({ message = "Something went wrong", onRetry }) {
  return (
    <Alert
      icon={<IconAlertCircle size={20} />}
      color="red"
      variant="light"
      radius="md"
      withCloseButton
      title="Error"
      style={{
        marginBottom: "1rem",
        border: "1px solid var(--mantine-color-red-4)",
      }}
    >
      <Group justify="space-between" align="center" wrap="nowrap">
        <Text size="sm">{message}</Text>

        {onRetry && (
          <Button
            size="xs"
            color="red"
            variant="filled"
            onClick={onRetry}
            radius="md"
          >
            Retry
          </Button>
        )}
      </Group>
    </Alert>
  );
}
