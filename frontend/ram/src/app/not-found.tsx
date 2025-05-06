import {
  Button,
  Container,
  Title,
  Text,
  Group,
  Center,
  Stack,
} from "@mantine/core";
import Link from "next/link";

export default function NotFound() {
  return (
    <Center>
      <Container fluid h={50}>
        <Stack align="center">
          <Title>404</Title>
          <Text c="dimmed" size="lg">
            Oops! The page you're looking for doesn't exist.
          </Text>
          <Text c="dimmed" size="lg">
            I haven't created it yet. :)
          </Text>
          <Button component={Link} href="/" size="md">
            Take me back to home page
          </Button>
        </Stack>
      </Container>
    </Center>
  );
}
