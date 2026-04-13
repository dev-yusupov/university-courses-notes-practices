import socket
import struct
import sys
import random
import select

def main():
    if len(sys.argv) != 3:
        print(f"Usage: {sys.argv[0]} <hostname> <port_number>")
        sys.exit(1)

    hostname = sys.argv[1]
    port = int(sys.argv[2])

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.setblocking(False)
    server_socket.bind((hostname, port))
    server_socket.listen(5)

    print(f"Server listening on {hostname}:{port}")

    inputs = [server_socket]
    recv_buffers = {}
    secret_number = random.randint(1, 100)
    print(f"Secret number is: {secret_number}")
    game_over = False

    packer = struct.Struct('c i')

    while inputs:
        readable, _, exceptional = select.select(inputs, [], inputs)

        for s in exceptional:
            if s is server_socket:
                continue
            try:
                peer = s.getpeername()
            except OSError:
                peer = '<disconnected>'
            print(f"Socket exception from {peer}. Closing connection.")
            recv_buffers.pop(s, None)
            if s in inputs:
                inputs.remove(s)
            try:
                s.close()
            except OSError:
                pass

        for s in readable:
            if s is server_socket:
                conn, addr = server_socket.accept()
                print(f"New connection from {addr}")
                conn.setblocking(False)
                inputs.append(conn)
                recv_buffers[conn] = bytearray()
            else:
                try:
                    chunk = s.recv(4096)
                    if not chunk:
                        print(f"Closing connection to {s.getpeername()} (client disconnected)")
                        recv_buffers.pop(s, None)
                        inputs.remove(s)
                        s.close()
                        continue

                    recv_buffers[s].extend(chunk)

                    while len(recv_buffers[s]) >= packer.size:
                        frame = bytes(recv_buffers[s][:packer.size])
                        del recv_buffers[s][:packer.size]

                        op_b, num = packer.unpack(frame)
                        op = op_b.decode(errors='replace')
                        print(f"Received from {s.getpeername()}: ('{op}', {num})")

                        if game_over:
                            response_char = b'V'
                        elif op == '<':
                            response_char = b'I' if secret_number < num else b'N'
                        elif op == '>':
                            response_char = b'I' if secret_number > num else b'N'
                        elif op == '=':
                            if secret_number == num:
                                response_char = b'Y'
                                game_over = True
                                print("A client guessed the number. Game over.")
                            else:
                                response_char = b'K'
                        else:
                            response_char = b'K'

                        s.sendall(packer.pack(response_char, 0))
                        if response_char in (b'Y', b'K', b'V'):
                            print(f"Closing connection to {s.getpeername()}")
                            recv_buffers.pop(s, None)
                            inputs.remove(s)
                            s.close()
                            break
                except ConnectionResetError:
                    print(f"Connection reset by {s.getpeername()}. Closing connection.")
                    recv_buffers.pop(s, None)
                    inputs.remove(s)
                    s.close()
                except Exception as e:
                    print(f"An error occurred with {s.getpeername()}: {e}")
                    recv_buffers.pop(s, None)
                    inputs.remove(s)
                    s.close()

    server_socket.close()
    print("Server shutting down.")

if __name__ == "__main__":
    main()
