"use client";
import {
  Box,
  Card,
  createTheme,
  Group,
  MantineProvider,
  Text,
  Title,
} from "@mantine/core";
import { Link } from "@mantine/tiptap";
import Document from "@tiptap/extension-document";
import { Dropcursor } from "@tiptap/extension-dropcursor";
import Highlight from "@tiptap/extension-highlight";
import Image from "@tiptap/extension-image";
import Paragraph from "@tiptap/extension-paragraph";
import Superscript from "@tiptap/extension-superscript";
import TextTiptap from "@tiptap/extension-text";
import TextAlign from "@tiptap/extension-text-align";
import { Color, TextStyle } from "@tiptap/extension-text-style";
import { useEditor } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
import { PostData } from "../../lib/definitions";

type PostProps = {
  post: PostData;
  allowHtmlMarkup: boolean;
};

const theme = createTheme({
  headings: {
    textWrap: "wrap",
  },
  components: {
    Title: {
      styles: () => ({
        root: {
          textTransform: "capitalize",
        },
      }),
    },
  },
});

export default function Post({ post, allowHtmlMarkup }: PostProps) {
  const editor = useEditor({
    shouldRerenderOnTransaction: true,

    extensions: [
      StarterKit,
      TextStyle,
      Color,
      Link,
      Superscript,
      Highlight,
      TextAlign.configure({ types: ["heading", "paragraph"] }),
      Image.configure({
        resize: {
          enabled: false,
          alwaysPreserveAspectRatio: true,
        },
      }),
      Document,
      Paragraph,
      TextTiptap,
      Dropcursor,
    ],
    content: post.content,
    immediatelyRender: false,
  });

  return (
    <MantineProvider theme={theme}>
      <Card padding="lg" styles={{ root: { marginBottom: 10 } }} id={post.id}>
        <Group justify="center" mb="md" align="right">
          <Title order={1}>{post.title}</Title>
        </Group>

        <Box
          size="sm"
          bd="1px solid #ddd"
          bdrs="md"
          dangerouslySetInnerHTML={{ __html: post.content }}
        />

        <br />
        <Text size="xs" ta="right">
          {post.author}
        </Text>
        <Text size="xs" ta="right">
          {post.createdOn}
        </Text>
      </Card>
    </MantineProvider>
  );
}
