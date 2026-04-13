import socket
import struct
import sys
import time


def recv_exact(sock, nbytes):
    data = bytearray()
    while len(data) < nbytes:
        chunk = sock.recv(nbytes - len(data))
        if not chunk:
            return None
        data.extend(chunk)
    return bytes(data)

def guess_number(sock, packer):
    low = 1
    high = 100

    while low < high:
        mid = (low + high) // 2
        request_data = packer.pack(b'>', mid)
        sock.sendall(request_data)

        response_data = recv_exact(sock, packer.size)
        if not response_data:
            print("Server closed connection unexpectedly.")
            return

        response_char, _ = packer.unpack(response_data)
        response_char = response_char.decode(errors='replace')

        if response_char == 'I':
            low = mid + 1
        elif response_char == 'N':
            high = mid
        elif response_char in ('Y', 'K', 'V'):
            if response_char == 'V':
                print("Game has ended. Exiting.")
            elif response_char == 'Y':
                print("I won!")
            else:
                print("I lost!")
            return
        else:
            print(f"Unexpected response from server: {response_char}. Exiting.")
            return

        time.sleep(0.05)

    guess = low
    request_data = packer.pack(b'=', guess)
    sock.sendall(request_data)

    response_data = recv_exact(sock, packer.size)
    if not response_data:
        print("Server closed connection unexpectedly.")
        return

    response_char, _ = packer.unpack(response_data)
    response_char = response_char.decode(errors='replace')

    if response_char == 'Y':
        print("I won!")
    elif response_char == 'K':
        print("I lost!")
    elif response_char == 'V':
        print("Game has already ended.")
    else:
        print(f"Final unexpected response: {response_char}")


def main():
    if len(sys.argv) != 3:
        print(f"Usage: {sys.argv[0]} <hostname> <port_number>")
        sys.exit(1)

    hostname = sys.argv[1]
    port = int(sys.argv[2])

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    try:
        client_socket.connect((hostname, port))
        print(f"Connected to server at {hostname}:{port}")

        # Match assignment "struct" layout: native sizes + native alignment (typically 8 bytes: c + padding + i).
        # Requirement: do NOT use '!'.
        packer = struct.Struct('c i')
        guess_number(client_socket, packer)


    except ConnectionRefusedError:
        print("Connection refused. Is the server running?")
    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        client_socket.close()
        print("Connection closed.")

if __name__ == "__main__":
    main()
